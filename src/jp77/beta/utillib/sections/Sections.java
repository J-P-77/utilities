/*
 * The MIT License (MIT)
 * 
 * Copyright (c) 2014 Justin Palinkas
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package jp77.beta.utillib.sections;

import jp77.utillib.collections.MyStackAll;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * File Structure:
 * 
 * [NAME1] Key1=Value Key2=Value Key3=Value
 * 
 * [NAME2] Key1=Value Key2=Value Key3=Value
 * 
 * @author Justin Palinkas
 */
public class Sections {
	private MyStackAll<Section> _Sections = new MyStackAll<Section>();

	public void addSection(Section title) {
		if(title != null) {
			int TitleIndex = findSection(title.getName());

			if(TitleIndex == -1) {
				_Sections.push(title);
			} else {
				for(int X = 0; X < title.length(); X++) {
					_Sections.getItemAt(TitleIndex).addProperty(title.getProperty(X));
				}
			}
		}
	}

	public void addSection(Section[] titles) {
		for(int X = 0; X < titles.length; X++) {
			addSection(titles[X]);
		}
	}

	public void addPropToSection(String titlename, String argname, String argvariable) {
		int TitleIndex = findSection(titlename);

		if(TitleIndex == -1) {//Not Found
			Section T_Title = new Section(titlename);

			T_Title.addProperty(argname, argvariable);

			_Sections.push(T_Title);
		} else {//Found
			_Sections.getItemAt(TitleIndex).addProperty(argname, argvariable);
		}
	}

	public Section popSection(String titlename) {
		int Index = findSection(titlename);

		return (Index == -1 ? null : _Sections.popItemAt(Index));
	}

	public int findSection(String titlename) {
		for(int X = 0; X < _Sections.length(); X++) {
			if(_Sections.getItemAt(X).getName().equalsIgnoreCase(titlename)) {
				return X;
			}
		}

		return -1;
	}

	public Section getSection(String titlename) {
		int Index = findSection(titlename);

		if(Index == -1) {
			return null;
		} else {
			return _Sections.getItemAt(Index);
		}
	}

	public int findProperty(String titlename, String argumentname) {
		for(int X = 0; X < _Sections.length(); X++) {
			if(_Sections.getItemAt(X).getName().equalsIgnoreCase(titlename)) {
				for(int Y = 0; Y < _Sections.getItemAt(X).length(); Y++) {
					if(_Sections.getItemAt(X).getProperty(Y).getName().equalsIgnoreCase(argumentname)) {
						return Y;
					}
				}
			}
		}

		return -1;
	}

	public boolean sectionExists(String titlename) {
		for(int X = 0; X < _Sections.length(); X++) {
			if(_Sections.getItemAt(X).getName().equalsIgnoreCase(titlename)) {
				return true;
			}
		}

		return false;
	}

	public boolean propertyExists(String titlename, String argumentname) {
		for(int X = 0; X < _Sections.length(); X++) {
			if(_Sections.getItemAt(X).getName().equalsIgnoreCase(titlename)) {
				for(int Y = 0; Y < _Sections.getItemAt(X).length(); X++) {
					if(_Sections.getItemAt(X).getProperty(Y).getName().equalsIgnoreCase(argumentname)) {
						return true;
					}
				}
			}
		}

		return false;
	}

	public SectionProperty getProperty(int titleindex, int argindex) {
		if(_Sections.validIndex(titleindex)) {
			Section T_Title = _Sections.getItemAt(titleindex);

			if(T_Title.getProperties().validIndex(argindex)) {
				return _Sections.getItemAt(titleindex).getProperty(argindex);
			}
		}

		return null;
	}

	public SectionProperty getProperty(String titlename, int argindex) {
		int TitleIndex = findSection(titlename);

		if(TitleIndex != -1) {
			Section T_Title = _Sections.getItemAt(TitleIndex);

			if(T_Title.getProperties().validIndex(argindex)) {
				return _Sections.getItemAt(TitleIndex).getProperty(argindex);
			}
		}

		return null;
	}

	public SectionProperty getProperty(String titlename, String argname) {
		for(int X = 0; X < _Sections.length(); X++) {
			if(titlename.equalsIgnoreCase(_Sections.getItemAt(X).getName())) {
				for(int Y = 0; Y < _Sections.getItemAt(X).length(); Y++) {
					if(argname.equalsIgnoreCase(_Sections.getItemAt(X).getProperty(Y).getName())) {
						return _Sections.getItemAt(X).getProperty(Y);
					}
				}
			}
		}

		return null;
	}

	public Section getSection(int titleindex) {
		if(_Sections.validIndex(titleindex)) {
			return _Sections.getItemAt(titleindex);
		}

		return null;
	}

	public int getSectionIndex(String titlename) {
		return findSection(titlename);
	}

	public int length() {
		return _Sections.length();
	}

	//STATIC

	public static Sections read(Sections titles, String path) {
		return read(titles, new File(path), false);
	}

	public static Sections read(Sections titles, File file) {
		return read(titles, file, false);
	}

	public static Sections read(Sections titles, File file, boolean autocase) {
		if(file.exists() && file.isFile()) {
			SectionInputStream IStream = null;
			try {
				IStream = new SectionInputStream(new FileInputStream(file));

				return read(titles, IStream, autocase);
			} catch(Exception e) {
				System.out.println("!!!ERROR!!!" + e.getMessage());
			} finally {
				try {
					if(IStream != null) {
						IStream.close();
						IStream = null;
					}
				} catch(Exception e) {}
			}
		}

		return null;
	}

	public static Sections read(Sections sections, InputStream ostream) {
		return read(sections, new SectionInputStream(ostream), false);
	}

	public static Sections read(Sections sections, InputStream ostream, boolean autocase) {
		return read(sections, new SectionInputStream(ostream), autocase);
	}

	/**
	 * 
	 * @param sections
	 * @param ostream
	 * @param autocase
	 * @return null if an error occurs
	 */
	public static Sections read(Sections sections, SectionInputStream istream, boolean autocase) {
		if(istream != null) {
			try {
				Section Section = null;
				while((Section = istream.readSection(autocase)) != null) {
					SectionProperty Prop = null;
					while((Prop = istream.readSectionProperty()) != null) {
						Section.addProperty(Prop);
					}

					sections.addSection(Section);
				}

				return sections;
			} catch(Exception e) {
				System.out.println("!!!ERROR!!! - " + e.getMessage());
			}
		}

		return null;
	}

	public static Sections write(Sections sections, String path) {
		return write(sections, new File(path), false, false);
	}

	public static Sections write(Sections sections, File file) {
		return write(sections, file, false, false);
	}

	public static Sections write(Sections sections, String path, boolean append) {
		return write(sections, new File(path), false, append);
	}

	public static Sections write(Sections sections, File file, boolean append) {
		return write(sections, file, false, append);
	}

	public static Sections write(Sections sections, File path, boolean autocase, boolean append) {
		SectionOutputStream OStream = null;
		try {
			OStream = new SectionOutputStream(new FileOutputStream(path, append));

			return write(sections, OStream, autocase);
		} catch(Exception e) {
			System.out.println("!!!ERROR!!!" + e.getMessage());
		} finally {
			try {
				if(OStream != null) {
					OStream.close();
					OStream = null;
				}
			} catch(Exception e) {}
		}

		return null;
	}

	public static Sections write(Sections sections, OutputStream ostream) {
		return write(sections, new SectionOutputStream(ostream), false);
	}

	public static Sections write(Sections sections, OutputStream ostream, boolean autocase) {
		return write(sections, new SectionOutputStream(ostream), autocase);
	}

	/**
	 * 
	 * @param sections
	 * @param ostream
	 * @param autocase
	 * @return null if an error occurs
	 */
	public static Sections write(Sections sections, SectionOutputStream ostream, boolean autocase) {
		try {
			for(int X = 0; X < sections.length(); X++) {
				if(X != 0) {
					ostream.newline();
				}
				final Section SECTION = sections.getSection(X);

				ostream.writeSection(SECTION, false, autocase);

				for(int Y = 0; Y < SECTION.length(); Y++) {
					ostream.writeProperty(SECTION.getProperty(Y));
				}
			}

			return sections;
		} catch(Exception e) {
			System.out.println("!!!ERROR!!!" + e.getMessage());
		}

		return null;
	}

	/*public static void main(String[] args) {
	    final Titles TITLES = new Titles();
	    
	    ArgumentInputStream IStream = null;
	    try {
	        IStream = new ArgumentInputStream(new FileInputStream("C:\\Documents and Settings\\Dalton Dell\\Desktop\\Java Tests\\Setting Test File.stf"));
	        read(TITLES, IStream, false);
	    } catch (Exception e) {
	        e.printStackTrace();
	    } finally {
	        try {
	            if(IStream != null) {
	                IStream.close();
	                IStream = null;
	            }
	        } catch (Exception e) {}
	    }

	    System.out.println("");
	}*/
}
