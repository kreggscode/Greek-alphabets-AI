import 'dart:io';
import 'dart:math' as math;
import 'dart:typed_data';

import 'package:image/image.dart' as img;

/// Generate BMI Pro icons at multiple resolutions without launching Flutter.
///
/// Run with:
/// ```
/// dart run tool/generate_icon.dart
/// ```
Future<void> main(List<String> args) async {
  final outputDir = Directory('tool/generated_icons');
  if (!outputDir.existsSync()) {
    outputDir.createSync(recursive: true);
  }

  const baseSize = 512;
  const sizes = <int>[512, 256, 128, 64, 48, 32, 24, 16];
  final entries = <_PngEntry>[];

  final master = _renderIcon(baseSize);

  for (final size in sizes) {
    final image = size == baseSize
        ? img.Image.from(master)
        : img.copyResize(
            master,
            width: size,
            height: size,
            interpolation: img.Interpolation.cubic,
          );
    final png = img.encodePng(image);

    final file = File('${outputDir.path}/bmi_pro_icon_$size.png');
    await file.writeAsBytes(png, flush: true);
    stdout.writeln('✓ Wrote ${file.path}');

    entries.add(_PngEntry(size: size, data: Uint8List.fromList(png)));
  }

  final icoBytes = _encodeIco(entries);
  final icoFile = File('${outputDir.path}/app_icon.ico');
  await icoFile.writeAsBytes(icoBytes, flush: true);
  stdout.writeln('✓ Wrote ${icoFile.path} (sizes: ${sizes.join(", ")})');

  stdout.writeln(
    '\nCopy app_icon.ico into windows/runner/resources/app_icon.ico, run '
    '`flutter clean`, rebuild, then repin shortcuts so Windows refreshes '
    'its cache.',
  );
}

img.Image _renderIcon(int size) {
  final image = img.Image(width: size, height: size); // defaults to transparent

  final inset = (size * 0.12).round();
  final radius = size * 0.22;
  final gradientSpan = math.max(1.0, (size - inset * 2).toDouble());
  final gradientDenominator = gradientSpan * 2;

  for (var y = 0; y < size; y++) {
    for (var x = 0; x < size; x++) {
      if (!_insideRoundedRect(
        x + 0.5,
        y + 0.5,
        inset.toDouble(),
        (size - inset).toDouble(),
        radius,
      )) {
        continue;
      }

      final t = ((x - inset) + (y - inset)) / gradientDenominator;
      final color = _lerpRgba(_topColor, _bottomColor, t);
      image.setPixelRgba(x, y, color.$1, color.$2, color.$3, 255);
    }
  }

  final highlightHeight = (size * 0.32).round();
  final highlightCenterY = inset + (highlightHeight * 0.45);
  final highlightRadiusX = size * 0.48;
  final highlightRadiusY = highlightHeight * 0.7;
  for (var y = inset; y < inset + highlightHeight; y++) {
    for (var x = inset; x < size - inset; x++) {
      if (!_insideRoundedRect(
        x + 0.5,
        y + 0.5,
        inset.toDouble(),
        (size - inset).toDouble(),
        radius,
      )) {
        continue;
      }
      final dx = (x - size / 2) / highlightRadiusX;
      final dy = (y - highlightCenterY) / highlightRadiusY;
      final d = dx * dx + dy * dy;
      if (d <= 1) {
        final k = (1 - d).clamp(0, 1) * 0.35;
        final pixel = image.getPixel(x, y);
        pixel
          ..r = (pixel.r + (255 - pixel.r) * k).round()
          ..g = (pixel.g + (255 - pixel.g) * k).round()
          ..b = (pixel.b + (255 - pixel.b) * k).round();
      }
    }
  }

  final shadowHeight = (size * 0.28).round();
  for (var y = size - inset - shadowHeight; y < size - inset; y++) {
    for (var x = inset; x < size - inset; x++) {
      if (!_insideRoundedRect(
        x + 0.5,
        y + 0.5,
        inset.toDouble(),
        (size - inset).toDouble(),
        radius,
      )) {
        continue;
      }
      final normalized =
          (y - (size - inset - shadowHeight)) / shadowHeight.toDouble();
      final k = (normalized.clamp(0, 1) * 0.45);
      final pixel = image.getPixel(x, y);
      pixel
        ..r = (pixel.r * (1 - k)).round()
        ..g = (pixel.g * (1 - k)).round()
        ..b = (pixel.b * (1 - k)).round();
    }
  }

  img.drawRect(
    image,
    x1: inset,
    y1: inset,
    x2: size - inset - 1,
    y2: size - inset - 1,
    radius: radius.round(),
    thickness: 7,
    color: img.ColorInt32.rgba(255, 255, 255, 120),
  );

  _drawHeart(image, size);

  // Label (render high-res, trim, scale, then composite).
  final labelCanvas = img.Image(width: size, height: (size * 0.3).round());
  img.drawString(
    labelCanvas,
    'BMI Pro',
    font: img.arial48,
    x: 0,
    y: 0,
    color: img.ColorInt32.rgba(255, 255, 255, 255),
  );

  final trimmed = img.trim(
    labelCanvas,
    mode: img.TrimMode.transparent,
    sides: img.Trim.all,
  );

  final desiredHeight = math.max(1, (size * 0.24).round());
  final scaled = img.copyResize(
    trimmed,
    height: desiredHeight,
    interpolation: img.Interpolation.cubic,
  );

  for (var y = 0; y < scaled.height; y++) {
    for (var x = 0; x < scaled.width; x++) {
      final pixel = scaled.getPixel(x, y);
      final alpha = pixel.a / 255.0;
      if (alpha == 0) continue;
      pixel
        ..r = math.min(255, pixel.r + 18)
        ..g = math.min(255, pixel.g + 18)
        ..b = math.min(255, pixel.b + 18);
    }
  }

  final shadow = img.Image(width: scaled.width, height: scaled.height);
  for (var y = 0; y < scaled.height; y++) {
    for (var x = 0; x < scaled.width; x++) {
      final source = scaled.getPixel(x, y);
      if (source.a == 0) continue;
      shadow.setPixelRgba(x, y, 0, 0, 0, (source.a * 0.35).round());
    }
  }

  final tx = math.max(0, ((size - scaled.width) / 2).round());
  final ty = math.max(0, (size * 0.77).round());
  final shadowOffset = ty + math.max(1, (size * 0.01).round());
  final shadowY =
      shadowOffset.clamp(0, math.max(0, size - shadow.height)) as int;
  img.compositeImage(image, shadow, dstX: tx, dstY: shadowY);
  img.compositeImage(image, scaled, dstX: tx, dstY: ty);

  return image;
}

const _topColor = (0x90, 0xA4, 0xFF);
const _bottomColor = (0x75, 0x3C, 0xFF);

(int, int, int) _lerpRgba(
  (int, int, int) a,
  (int, int, int) b,
  double t,
) {
  t = t.clamp(0.0, 1.0);
  final r = (a.$1 + (b.$1 - a.$1) * t).round().clamp(0, 255);
  final g = (a.$2 + (b.$2 - a.$2) * t).round().clamp(0, 255);
  final bOut = (a.$3 + (b.$3 - a.$3) * t).round().clamp(0, 255);
  return (r, g, bOut);
}

bool _insideRoundedRect(
  double x,
  double y,
  double min,
  double max,
  double radius,
) {
  final left = min;
  final right = max;
  final top = min;
  final bottom = max;

  if (x >= left + radius && x <= right - radius) {
    return y >= top && y <= bottom;
  }
  if (y >= top + radius && y <= bottom - radius) {
    return x >= left && x <= right;
  }

  final cx = x < left + radius ? left + radius : right - radius;
  final cy = y < top + radius ? top + radius : bottom - radius;
  final dx = x - cx;
  final dy = y - cy;
  return (dx * dx + dy * dy) <= radius * radius;
}

double _distanceToRoundedRectEdge(
  double x,
  double y,
  double min,
  double max,
  double radius,
) {
  final left = min;
  final right = max;
  final top = min;
  final bottom = max;

  if (x >= left + radius && x <= right - radius) {
    return math.min(y - top, bottom - y);
  }
  if (y >= top + radius && y <= bottom - radius) {
    return math.min(x - left, right - x);
  }

  final cx = x < left + radius ? left + radius : right - radius;
  final cy = y < top + radius ? top + radius : bottom - radius;
  final dx = (x - cx).abs();
  final dy = (y - cy).abs();
  return radius - math.sqrt(dx * dx + dy * dy);
}

void _drawHeart(img.Image image, int size) {
  final heartSize = (size * 0.42).round();
  final detailSize = math.max(heartSize * 2, 8);
  final heartHighRes = img.Image(width: detailSize, height: detailSize);

  for (var y = 0; y < detailSize; y++) {
    for (var x = 0; x < detailSize; x++) {
      final fx = x / (detailSize - 1);
      final fy = y / (detailSize - 1);
      final centerX = 0.5;
      final topY = 0.32;
      final radius = 0.23;
      final leftCenterX = centerX - radius;
      final rightCenterX = centerX + radius;

      final dxLeft = fx - leftCenterX;
      final dxRight = fx - rightCenterX;
      final dy = fy - topY;
      final inLeft = dxLeft * dxLeft + dy * dy <= radius * radius;
      final inRight = dxRight * dxRight + dy * dy <= radius * radius;

      final bottomStart = topY;
      final bottomEnd = 0.96;
      final slope = ((bottomEnd - fy) / (bottomEnd - bottomStart))
          .clamp(0.0, 1.0);
      final halfWidth = radius * 1.45 * slope;
      final inBottom =
          fy >= bottomStart && (fx - centerX).abs() <= halfWidth;

      if (inLeft || inRight || inBottom) {
        heartHighRes.setPixelRgba(x, y, 255, 255, 255, 255);
      }
    }
  }

  final heart = img.copyResize(
    heartHighRes,
    width: heartSize,
    height: heartSize,
    interpolation: img.Interpolation.cubic,
  );

  final tx = ((size - heart.width) / 2).round();
  final ty = (size * 0.34).round() - heart.height ~/ 2;
  final heartY = ty.clamp(0, math.max(0, size - heartSize)) as int;
  img.compositeImage(image, heart, dstX: tx, dstY: heartY);
}

Uint8List _encodeIco(List<_PngEntry> entries) {
  final count = entries.length;
  var offset = 6 + count * 16;
  final buffer = BytesBuilder();

  buffer.add([0, 0]); // reserved
  buffer.add([1, 0]); // icon type
  buffer.add([count, 0]);

  final directory = BytesBuilder();
  for (final entry in entries) {
    final sizeByte = entry.size == 256 ? 0 : entry.size;
    directory.add([
      sizeByte,
      sizeByte,
      0,
      0,
      1,
      0,
      32,
      0,
      ..._uint32ToBytes(entry.data.length),
      ..._uint32ToBytes(offset),
    ]);
    offset += entry.data.length;
  }
  buffer.add(directory.toBytes());

  for (final entry in entries) {
    buffer.add(entry.data);
  }

  return buffer.toBytes();
}

List<int> _uint32ToBytes(int value) => [
      value & 0xFF,
      (value >> 8) & 0xFF,
      (value >> 16) & 0xFF,
      (value >> 24) & 0xFF,
    ];

class _PngEntry {
  const _PngEntry({required this.size, required this.data});

  final int size;
  final Uint8List data;
}

