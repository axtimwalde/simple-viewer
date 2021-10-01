/**
 *                         THE CRAPL v0 BETA 1
 *
 *
 * 0. Information about the CRAPL
 *
 * If you have questions or concerns about the CRAPL, or you need more
 * information about this license, please contact:
 *
 *    Matthew Might
 *    http://matt.might.net/
 *
 *
 * I. Preamble
 *
 * Science thrives on openness.
 *
 * In modern science, it is often infeasible to replicate claims without
 * access to the software underlying those claims.
 *
 * Let's all be honest: when scientists write code, aesthetics and
 * software engineering principles take a back seat to having running,
 * working code before a deadline.
 *
 * So, let's release the ugly.  And, let's be proud of that.
 *
 *
 * II. Definitions
 *
 * 1. "This License" refers to version 0 beta 1 of the Community
 *     Research and Academic Programming License (the CRAPL).
 *
 * 2. "The Program" refers to the medley of source code, shell scripts,
 *     executables, objects, libraries and build files supplied to You,
 *     or these files as modified by You.
 *
 *    [Any appearance of design in the Program is purely coincidental and
 *     should not in any way be mistaken for evidence of thoughtful
 *     software construction.]
 *
 * 3. "You" refers to the person or persons brave and daft enough to use
 *     the Program.
 *
 * 4. "The Documentation" refers to the Program.
 *
 * 5. "The Author" probably refers to the caffeine-addled graduate
 *     student that got the Program to work moments before a submission
 *     deadline.
 *
 *
 * III. Terms
 *
 * 1. By reading this sentence, You have agreed to the terms and
 *    conditions of this License.
 *
 * 2. If the Program shows any evidence of having been properly tested
 *    or verified, You will disregard this evidence.
 *
 * 3. You agree to hold the Author free from shame, embarrassment or
 *    ridicule for any hacks, kludges or leaps of faith found within the
 *    Program.
 *
 * 4. You recognize that any request for support for the Program will be
 *    discarded with extreme prejudice.
 *
 * 5. The Author reserves all rights to the Program, except for any
 *    rights granted under any additional licenses attached to the
 *    Program.
 *
 *
 * IV. Permissions
 *
 * 1. You are permitted to use the Program to validate published
 *    scientific claims.
 *
 * 2. You are permitted to use the Program to validate scientific claims
 *    submitted for peer review, under the condition that You keep
 *    modifications to the Program confidential until those claims have
 *    been published.
 *
 * 3. You are permitted to use and/or modify the Program for the
 *    validation of novel scientific claims if You make a good-faith
 *    attempt to notify the Author of Your work and Your claims prior to
 *    submission for publication.
 *
 * 4. If You publicly release any claims or data that were supported or
 *    generated by the Program or a modification thereof, in whole or in
 *    part, You will release any inputs supplied to the Program and any
 *    modifications You made to the Progam.  This License will be in
 *    effect for the modified program.
 *
 *
 * V. Disclaimer of Warranty
 *
 * THERE IS NO WARRANTY FOR THE PROGRAM, TO THE EXTENT PERMITTED BY
 * APPLICABLE LAW. EXCEPT WHEN OTHERWISE STATED IN WRITING THE COPYRIGHT
 * HOLDERS AND/OR OTHER PARTIES PROVIDE THE PROGRAM "AS IS" WITHOUT
 * WARRANTY OF ANY KIND, EITHER EXPRESSED OR IMPLIED, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 * A PARTICULAR PURPOSE. THE ENTIRE RISK AS TO THE QUALITY AND
 * PERFORMANCE OF THE PROGRAM IS WITH YOU. SHOULD THE PROGRAM PROVE
 * DEFECTIVE, YOU ASSUME THE COST OF ALL NECESSARY SERVICING, REPAIR OR
 * CORRECTION.
 *
 *
 * VI. Limitation of Liability
 *
 * IN NO EVENT UNLESS REQUIRED BY APPLICABLE LAW OR AGREED TO IN WRITING
 * WILL ANY COPYRIGHT HOLDER, OR ANY OTHER PARTY WHO MODIFIES AND/OR
 * CONVEYS THE PROGRAM AS PERMITTED ABOVE, BE LIABLE TO YOU FOR DAMAGES,
 * INCLUDING ANY GENERAL, SPECIAL, INCIDENTAL OR CONSEQUENTIAL DAMAGES
 * ARISING OUT OF THE USE OR INABILITY TO USE THE PROGRAM (INCLUDING BUT
 * NOT LIMITED TO LOSS OF DATA OR DATA BEING RENDERED INACCURATE OR
 * LOSSES SUSTAINED BY YOU OR THIRD PARTIES OR A FAILURE OF THE PROGRAM
 * TO OPERATE WITH ANY OTHER PROGRAMS), EVEN IF SUCH HOLDER OR OTHER
 * PARTY HAS BEEN ADVISED OF THE POSSIBILITY OF SUCH DAMAGES.
 *
 *
 */
package org.janelia.saalfeldlab;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.WindowConstants;

import org.janelia.saalfeldlab.N5Factory.N5Options;
import org.janelia.saalfeldlab.n5.N5FSReader;
import org.janelia.saalfeldlab.n5.N5Reader;
import org.janelia.saalfeldlab.n5.N5Reader.Version;
import org.janelia.saalfeldlab.n5.imglib2.N5Utils;

import com.formdev.flatlaf.FlatDarculaLaf;

import bdv.util.AxisOrder;
import bdv.util.Bdv;
import bdv.util.BdvFunctions;
import bdv.util.BdvOptions;
import bdv.util.BdvStackSource;
import bdv.util.RandomAccessibleIntervalMipmapSource;
import bdv.util.volatiles.SharedQueue;
import bdv.util.volatiles.VolatileViews;
import mpicbg.spim.data.sequence.FinalVoxelDimensions;
import net.imglib2.RandomAccessible;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.cache.volatiles.CacheHints;
import net.imglib2.cache.volatiles.LoadingStrategy;
import net.imglib2.converter.Converter;
import net.imglib2.converter.Converters;
import net.imglib2.realtransform.AffineTransform3D;
import net.imglib2.transform.integer.MixedTransform;
import net.imglib2.type.NativeType;
import net.imglib2.type.numeric.ARGBType;
import net.imglib2.type.volatiles.AbstractVolatileNativeRealType;
import net.imglib2.type.volatiles.VolatileDoubleType;
import net.imglib2.util.Pair;
import net.imglib2.util.ValuePair;
import net.imglib2.view.IntervalView;
import net.imglib2.view.MixedTransformView;
import net.imglib2.view.Views;
import picocli.CommandLine;
import picocli.CommandLine.Option;

/**
 *
 *
 * @author Stephan Saalfeld &lt;saalfelds@janelia.hhmi.org&gt;
 */
public class View implements Callable<Void> {

	protected static class ReaderInfo {

		public final N5Reader n5;
		public final String[] groupNames;
		public final double[][] resolutions;
		public final double[][] contrastRanges;
		public final double[][] offsets;
		public final int[][] axess;

		public ReaderInfo(
				final N5Reader n5,
				final String[] groupNames,
				final double[][] resolutions,
				final double[][] contrastRanges,
				final double[][] offsets,
				final int[][] axess) {

			this.n5 = n5;
			this.groupNames = groupNames;
			this.resolutions = resolutions;
			this.contrastRanges = contrastRanges;
			this.offsets = offsets;
			this.axess = axess;
		}
	}

	@Option(names = {"-i", "--container"}, required = true, description = "container paths, e.g. -i $HOME/fib19.n5 -i /nrs/flyem ...")
	private List<String> containerPaths = null;

	@Option(names = {"-d", "--datasets"}, required = true, description = "comma separated list of datasets, one list per container, e.g. -d '/slab-26,slab-27' -d '/volumes/raw' ...")
	private List<String> groupLists = null;

	@Option(names = {"-r", "--resolution"}, description = "comma separated list of scale factors, one per dataset or all following the last, e.g. -r '4,4,40' (default 1,1[,1[,1]])")
	private List<String> resolutionStrings = null;

	@Option(names = {"-c", "--contrast"}, description = "comma separated contrast range to be mapped into [0,1000], one per dataset or all following the last, e.g. -c '0,255' (default 0,255)")
	private List<String> contrastStrings = null;

	@Option(names = {"-o", "--offset"}, description = "comma separated list of offsets (in scaled world coordinates), one per dataset or all following the last, e.g. -o '100.0,200.0,10.0' (default 0,0[,0[,0]])")
	private List<String> offsetStrings = null;

	@Option(names = {"-a", "--axes"}, description = "comma separated list of axes to be displayed as XY[Z[T]], one per dataset or all following the last, e.g. -a '0,2,1' (default 0,1[,2[,3]])")
	private List<String> axesStrings = null;

	@Option(names = {"-t", "--threads"}, description = "number of rendering threads, e.g. -t 4 (default 3)")
	private int numRenderingThreads = 3;

	@Option(names = {"-s", "--scales"}, split = ",", description = "comma separated list of screen scales, e.g. -s 1.0,0.5,0.25 (default 1.0,0.75,0.5,0.25,0.125)")
	private double[] screenScales = new double[] {1.0, 0.5, 0.25, 0.125};

	private int maxN = 2;

	private final ArrayList<ReaderInfo> readerInfos = new ArrayList<>();

	private static final boolean parseCSDoubleArray(final String csv, final double[] array) {

		final String[] stringValues = csv.split(",\\s*");
		try {
			final int n = Math.min(array.length, stringValues.length);
			for (int i = 0; i < n; ++i)
				array[i] = Double.parseDouble(stringValues[i]);
		} catch (final NumberFormatException e) {
			e.printStackTrace(System.err);
			return false;
		}
		return true;
	}

	private static final int[] parseCSIntArray(final String csv) {

		try {
			final String[] stringValues = csv.split(",\\s*");
			final int[] array = new int[stringValues.length];
			for (int i = 0; i < array.length; ++i)
				array[i] = Integer.parseInt(stringValues[i]);
			return array;
		} catch (final Exception e) {
			e.printStackTrace(System.err);
			return null;
		}
	}

	private static final double[] parseContrastRange(final String csv) {

		if (csv.toLowerCase().startsWith("label"))
			return null;
		else {
			final double[] array = new double[]{0, 255};
			parseCSDoubleArray(csv, array);
			return array;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public Void call() throws IOException, UnsupportedLookAndFeelException {

		UIManager.setLookAndFeel(new FlatDarculaLaf());

		maxN = 2;
		double[] resolution = new double[]{1, 1, 1, 1};
		double[] contrast = new double[]{0, 255};
		double[] offset = new double[]{0, 0, 0, 0};
		int[] axes = new int[]{0, 1, 2, 3};
		for (int i = 0, j = 0; i < containerPaths.size(); ++i) {
			final String containerPath = containerPaths.get(i);
			final N5Reader n5 = N5Factory.createN5Reader(new N5Options(containerPath, new int[] {64}, null));
			final String[] groups = groupLists.get(i).split(",\\s*");
			final double[][] resolutions = new double[groups.length][];
			final double[][] contrastRanges = new double[groups.length][];
			final double[][] offsets = new double[groups.length][];
			final int[][] axess = new int[groups.length][];
			for (int k = 0; k < groups.length; ++k, ++j) {
				if (contrastStrings != null && j < contrastStrings.size())
					contrast = parseContrastRange(contrastStrings.get(j));
				final int n = datasetN(n5, groups[k]);
				final double[] nextResolution = new double[n];
				Arrays.fill(nextResolution, 1);
				System.arraycopy(resolution, 0, nextResolution, 0, Math.min(resolution.length, n));
				resolution = nextResolution;
				if (resolutionStrings != null && j < resolutionStrings.size())
					parseCSDoubleArray(resolutionStrings.get(j), resolution);
				double[] nextOffset = datasetOffset(n5, groups[k]);
				if (nextOffset == null) {
					nextOffset = new double[n];
					Arrays.fill(nextOffset, 0);
					System.arraycopy(offset, 0, nextOffset, 0, Math.min(offset.length, n));
				}
				offset = nextOffset;
				if (offsetStrings != null && j < offsetStrings.size())
					parseCSDoubleArray(offsetStrings.get(j), offset);
				int[] nextAxes;
				if (axesStrings == null) {
					nextAxes = new int[n];
					Arrays.setAll(nextAxes, a -> a);
				} else {
					nextAxes = null;
					if (j < axesStrings.size())
						nextAxes = parseCSIntArray(axesStrings.get(j));
					if (nextAxes == null) {
						nextAxes = axes;
					}
				}
				axes = nextAxes;
				if (axes.length > maxN) maxN = axes.length;
					resolutions[k] = resolution.clone();
				contrastRanges[k] = contrast == null ? null : contrast.clone();
				offsets[k] = offset.clone();
				axess[k] = axes.clone();
			}
				readerInfos.add(new ReaderInfo(n5, groups, resolutions, contrastRanges, offsets, axess));
		}


		final int numProc = Runtime.getRuntime().availableProcessors();
		final SharedQueue queue = new SharedQueue(Math.min(8, Math.max(1, numProc / 2)));
		BdvStackSource<?> bdv = null;

		final BdvOptions options = Bdv.options();
		if (maxN == 2)
			options.is2D();
		else if (maxN == 4)
			options.axisOrder(AxisOrder.XYZT);

		options.numRenderingThreads(numRenderingThreads);
		options.screenScales(screenScales);

		int id = 0;
		for (final ReaderInfo entry : readerInfos) {

			final N5Reader n5 = entry.n5;
			for (int i = 0; i < entry.groupNames.length; ++i) {

				final String groupName = entry.groupNames[i];
				final double[] res = entry.resolutions[i];
				final double[] con = entry.contrastRanges[i];
				final boolean isLabel = con == null;
				final double[] off = entry.offsets[i];
				final int[] ax = entry.axess[i];

				System.out.println(n5 + " : " + groupName + ", " + Arrays.toString(res) + ", " + (isLabel ? " labels " : Arrays.toString(con)) + ", " + Arrays.toString(off) + ", Num axes: " + ax.length);

				@SuppressWarnings("rawtypes")
				final Pair<RandomAccessibleInterval<NativeType>[], double[][]> n5Sources;
				int n;
				if (n5.datasetExists(groupName)) {
					// this works for javac openjdk 8
					@SuppressWarnings({"rawtypes"})
					final RandomAccessibleInterval<NativeType> source = (RandomAccessibleInterval)N5Utils.openVolatile(n5, groupName);
					n = source.numDimensions();
					final double[] scale = new double[n];
					Arrays.fill(scale, 1);
					n5Sources = new ValuePair<>(new RandomAccessibleInterval[] {source}, new double[][]{scale});
				}
				else {
					n5Sources = N5Utils.openMipmaps(n5, groupName, true);
					n = n5Sources.getA()[0].numDimensions();
				}

				/* make volatile */
				@SuppressWarnings("rawtypes")
				final RandomAccessibleInterval<NativeType>[] ras = n5Sources.getA();
				@SuppressWarnings("rawtypes")
				final RandomAccessibleInterval[] vras = new RandomAccessibleInterval[ras.length];
				Arrays.setAll(vras, k ->
					VolatileViews.wrapAsVolatile(
							n5Sources.getA()[k],
							queue,
							new CacheHints(LoadingStrategy.VOLATILE, 0, true)));

				/* hyperslice and map axes */
				final int[] allAxes = allAxes(ax, n);

				Arrays.setAll(vras, k -> permuteAll(vras[k], allAxes));

				System.out.println("axes permutation: " + Arrays.toString(ax) + " -> " + Arrays.toString(allAxes));

				for (int d = axes.length; d < n; ++d) {
					for (int k = 0; k < vras.length; ++k)
						vras[k] = Views.hyperSlice(vras[k], ax.length, Math.round(off[allAxes[d]]));
				}
				for (int k = 0; k < vras.length; ++k)
					if (vras[k].numDimensions() < 3)
						vras[k] = Views.addDimension(vras[k], 0, 0);

				final double[] mappedOffset = new double[] {
						off[allAxes[0]],
						off[allAxes[1]],
						ax.length > 2 ? off[allAxes[2]] : 0
				};

				final RandomAccessibleInterval<VolatileDoubleType>[] convertedSources = new RandomAccessibleInterval[n5Sources.getA().length];
				for (int k = 0; k < vras.length; ++k) {
					final Converter<AbstractVolatileNativeRealType<?, ?>, VolatileDoubleType> converter;
					if (isLabel) {
						final int idHash = hash(id);
						converter = (a, b) -> {
							b.setValid(a.isValid());
							if (b.isValid()) {
								final int x = hash(Double.hashCode(a.get().getRealDouble()) ^ idHash);
								final double v = ((double)x / Integer.MAX_VALUE + 1) * 500.0;
								b.setReal(v);
							}
						};
					}
					else
						converter = (a, b) -> {
							b.setValid(a.isValid());
							if (b.isValid()) {
								double v = a.get().getRealDouble();
								v -= con[0];
								v /= con[1] - con[0];
								v *= 1000;
								b.setReal(v);
							}
						};
					convertedSources[k] = Converters.convert(
							(RandomAccessibleInterval<AbstractVolatileNativeRealType<?, ?>>)vras[k],
							converter,
							new VolatileDoubleType());
					final double[] scale = n5Sources.getB()[k];
					final double[] mappedScale = new double[] {
							scale[allAxes[0]] * res[0],
							scale[allAxes[1]] * res[1],
							ax.length > 2 ? scale[allAxes[2]] * res[2] : 1
					};
					n5Sources.getB()[k] = mappedScale;
				}

				/* offset transform */
				final AffineTransform3D sourceTransform = new AffineTransform3D();
				sourceTransform.setTranslation(mappedOffset);
				System.out.println(groupName + " " + sourceTransform.toString());

				final RandomAccessibleIntervalMipmapSource<VolatileDoubleType> mipmapSource =
						new RandomAccessibleIntervalMipmapSource<>(
								convertedSources,
								new VolatileDoubleType(),
								n5Sources.getB(),
								new FinalVoxelDimensions("px", res),
								sourceTransform,
								groupName);

				bdv = BdvFunctions.show(
						mipmapSource,
						bdv == null ? options : options.addTo(bdv));
				bdv.setDisplayRange(0, 1000);
				bdv.setColor(new ARGBType(argb(id++)));
			}

			if (id == 1)
				bdv.setColor(new ARGBType(0xffffffff));
		}

		((JFrame)SwingUtilities.getWindowAncestor(bdv.getBdvHandle().getViewerPanel())).setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

		return null;
	}

	private static final double[] rs = new double[]{1, 1, 0, 0, 0, 1, 1};
	private static final double[] gs = new double[]{0, 1, 1, 1, 0, 0, 0};
	private static final double[] bs = new double[]{0, 0, 0, 1, 1, 1, 0};

	final static private double goldenRatio = 1.0 / (0.5 * Math.sqrt(5) + 0.5);

	private static final double getDouble(final long id) {

		final double x = id * goldenRatio;
		return x - (long)Math.floor(x);
	}

	private static final int interpolate(final double[] xs, final int k, final int l, final double u, final double v) {

		return (int)((v * xs[k] + u * xs[l]) * 255.0 + 0.5);
	}

	private static final int argb(final int r, final int g, final int b, final int alpha) {

		return (((r << 8) | g) << 8) | b | alpha;
	}

	private static final int argb(final long id) {

		double x = getDouble(id);
		x *= 6.0;
		final int k = (int)x;
		final int l = k + 1;
		final double u = x - k;
		final double v = 1.0 - u;

		final int r = interpolate( rs, k, l, u, v );
		final int g = interpolate( gs, k, l, u, v );
		final int b = interpolate( bs, k, l, u, v );

		return argb( r, g, b, 0xff );
	}

	public static final void main(final String... args) {

		new CommandLine(new View()).execute(singlePathToArgs(args));
	}

	// hash code from https://stackoverflow.com/questions/664014/what-integer-hash-function-are-good-that-accepts-an-integer-hash-key
	private static final int hash(final int id) {
		int x = ((id >>> 16) ^ id) * 0x45d9f3b;
		x = ((x >>> 16) ^ x) * 0x45d9f3b;
		x = (x >>> 16) ^ x;
		return x;
	}

	/**
	 * Create a view with permuted axes as specified.
	 *
	 * TODO remove when available in ImgLib2
	 *
	 * <p>
	 * <em>Note, that it is not allowed to set the {@code axes} array such that
	 * a source component is mapped to several target components!</em>
	 * </p>
	 */
	private static final <T> IntervalView<T> permuteAll(final RandomAccessibleInterval<T> interval, final int... axes) {

		final int n = interval.numDimensions();

//		System.out.println("before: " + Arrays.toString(Intervals.dimensionsAsLongArray(interval)));

		assert n == axes.length : "The number of source dimensions must match the number of axes.";

		final long[] min = new long[n];
		final long[] max = new long[n];
		for (int d = 0; d < n; ++d) {
			min[d] = interval.min(axes[d]);
			max[d] = interval.max(axes[d]);
		}

//		System.out.println("after: " + Arrays.toString(Intervals.dimensionsAsLongArray(Views.interval(permuteAll((RandomAccessible<T>)interval, axes), min, max))));

		return Views.interval(permuteAll((RandomAccessible<T>)interval, axes), min, max);
	}

	/**
	 * Create a view with permuted axes as specified.
	 *
	 * TODO remove when available in ImgLib2
	 *
	 * <p>
	 * <em>Note, that it is not allowed to set the {@code axes} array such that
	 * a source component is mapped to several target components!</em>
	 * </p>
	 */
	private static final <T> MixedTransformView<T> permuteAll(final RandomAccessible<T> randomAccessible, final int... axes) {

		final int n = randomAccessible.numDimensions();

		assert n == axes.length : "The number of source dimensions must match the number of axes.";

		final MixedTransform t = new MixedTransform(n, n);
		t.setComponentMapping(axes);
		return new MixedTransformView<T>(randomAccessible, t);
	}

	private static final int[] allAxes(final int[] axes, final int n) {

		final int[] sortedAxes = axes.clone();
		Arrays.sort(sortedAxes);
		final int[] allAxes = Arrays.copyOf(axes, n);
		int b = sortedAxes.length;
		int c = 0;
		for (int a = 0; a < axes.length; ++c) {
			if (sortedAxes[a] > c)
				allAxes[b++] = c;
			else
				++a;
		}
		for (; c < allAxes.length; ++c, ++b)
			allAxes[b] = c;

		return allAxes;
	}

	private static final int datasetN(final N5Reader n5, final String group) throws IOException {

		if (n5.datasetExists(group))
			return n5.getAttribute(group, "dimensions", long[].class).length;
		else
			return n5.getAttribute(group + "/s0", "dimensions", long[].class).length;
	}

	private static final double[] datasetOffset(final N5Reader n5, final String group) {

		double[] offset;
		try {
			offset = n5.getAttribute(group, "offset", double[].class);
			if (offset == null)
				offset = n5.getAttribute(group + "/s0", "offset", double[].class);
			if (offset != null)
				offset = Arrays.copyOf(offset, datasetN(n5, group));
		} catch (final IOException e) {
			offset = null;
		}
		return offset;
	}

	private static final String[] singlePathToArgs(final String... args) {

		if (args.length == 1) {
			final Path path = Paths.get(args[0]).toAbsolutePath();
			if (
					Files.exists(path) &&
					Files.isDirectory(path)) {
				for (int i = path.getNameCount(); i > 0; --i) {
					final Path subpath = path.subpath(0, i);
					try {
						final String n5Path = "/" + subpath.toString();
						final N5FSReader n5 = new N5FSReader(n5Path);
						Version version;
						try {
							version = n5.getVersion();
						} catch (final IOException f) {
							f.printStackTrace(System.err);
							continue;
						}
						if (version != null && version.getMajor() > 0) {
							final String datasetPath = "/" + path.subpath(i, path.getNameCount()).toString();
							if (n5.exists(datasetPath)) {
								return new String[] {
										"-i",
										n5Path,
										"-d",
										datasetPath};
								}
						}
					} catch (final Exception e) {
						e.printStackTrace(System.err);
						return args;
					}
				}
			}
			return args;
		} else
			return args;
	}
}
