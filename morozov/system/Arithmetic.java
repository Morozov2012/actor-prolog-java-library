// (c) 2010 IRE RAS Alexei A. Morozov

package morozov.system;

import target.*;

import morozov.classes.*;
import morozov.terms.*;

import java.math.BigInteger;

public class Arithmetic {
	// "Check all" operations
	public static void check_all_arguments(ChoisePoint cp, Term[] array, CheckTermOperation operation) throws Backtracking {
		for(int i= 0; i < array.length; i++) {
			if (!operation.eval(cp,array[i].dereferenceValue(cp))) {
				throw new Backtracking();
			}
		}
	}
	public static void check_all_arguments(ChoisePoint cp, Term[] array, CheckTermOperation operation, ActiveWorld currentProcess) throws Backtracking {
		for(int i= 0; i < array.length; i++) {
			if (!operation.eval(cp,array[i].dereferenceValue(cp),currentProcess)) {
				throw new Backtracking();
			}
		}
	}
	// Comparison operations
	public static void compare_two_numbers(ChoisePoint iX, Term n1, Term n2, ComparisonOperation operation) throws Backtracking {
		try {
			BigInteger a= n1.getIntegerValue(iX);
			try {
				BigInteger b= n2.getIntegerValue(iX);
				if ( !operation.eval(a,b) ) {
					throw new Backtracking();
				}
			} catch (TermIsNotAnInteger b1) {
				try {
					double b= n2.getRealValue(iX);
					double ra= a.doubleValue();
					if ( !operation.eval(ra,b) ) {
						throw new Backtracking();
					}
				} catch (TermIsNotAReal b2) {
					//:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
					//:: First argument: integer, second argument: list.                 ::
					//:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
					try {
						Term head2= n2.getNextListHead(iX);
						Term tail2= n2.getNextListTail(iX);
						compare_item_and_list(iX,n1,operation,head2,tail2);
					} catch (EndOfList eol) {
						// return true;
					} catch (TermIsNotAList b3) {
						throw new WrongArgumentIsNotNumeric(n2);
					}
					//:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
				}
			}
		} catch (TermIsNotAnInteger a1) {
			try {
				double a= n1.getRealValue(iX);
				try {
					BigInteger b= n2.getIntegerValue(iX);
					double rb= b.doubleValue();
					if ( !operation.eval(a,rb) ) {
						throw new Backtracking();
					}
				} catch (TermIsNotAnInteger b1) {
					try {
						double b= n2.getRealValue(iX);
						if ( !operation.eval(a,b) ) {
							throw new Backtracking();
						}
					} catch (TermIsNotAReal b2) {
						//:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
						//:: First argument: real, second argument: list.                    ::
						//:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
						try {
							Term head2= n2.getNextListHead(iX);
							Term tail2= n2.getNextListTail(iX);
							compare_item_and_list(iX,n1,operation,head2,tail2);
						} catch (EndOfList eol) {
							return;
						} catch (TermIsNotAList b3) {
							throw new WrongArgumentIsNotNumeric(n2);
						}
						//:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
					}
       	                        }
			} catch (TermIsNotAReal a2) {
				try {
					String sa= n1.getStringValue(iX);
					try {
						String sb= n2.getStringValue(iX);
						if ( !operation.eval(sa,sb) ) {
							throw new Backtracking();
						}
					} catch (TermIsNotAString b1) {
						//:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
						//:: First argument: string, second argument: list.                  ::
						//:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
						try {
							Term head2= n2.getNextListHead(iX);
							Term tail2= n2.getNextListTail(iX);
							compare_item_and_list(iX,n1,operation,head2,tail2);
						} catch (EndOfList eol) {
							return;
						} catch (TermIsNotAList b2) {
							throw new WrongArgumentIsNotNumeric(n2);
						}
						//:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
					}
				} catch (TermIsNotAString a3) {
					try {
						long la= n1.getSymbolValue(iX);
						try {
							long lb= n2.getSymbolValue(iX);
							if ( !operation.eval(la,lb) ) {
								throw new Backtracking();
							}
						} catch (TermIsNotASymbol b1) {
							//:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
							//:: First argument: symbol, second argument: list.                  ::
							//:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
							try {
								Term head2= n2.getNextListHead(iX);
								Term tail2= n2.getNextListTail(iX);
								compare_item_and_list(iX,n1,operation,head2,tail2);
							} catch (EndOfList eol) {
								return;
							} catch (TermIsNotAList b2) {
								throw new WrongArgumentIsNotNumeric(n2);
							}
							//:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
						}
					} catch (TermIsNotASymbol a4) {
						try {
							BigInteger a= Converters.termToDate(n1,iX);
							try {
								BigInteger b= Converters.termToDate(n2,iX);
								if ( !operation.eval(a,b) ) {
									throw new Backtracking();
								}
							} catch (TermIsNotADate b1) {
								//:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
								//:: First argument: date, second argument: list.                    ::
								//:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
								try {
									Term head2= n2.getNextListHead(iX);
									Term tail2= n2.getNextListTail(iX);
									compare_item_and_list(iX,n1,operation,head2,tail2);
								} catch (EndOfList eol) {
									return;
								} catch (TermIsNotAList b2) {
									throw new WrongArgumentIsNotNumeric(n2);
								}
								//:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
							}
						} catch (TermIsNotADate a5) {
							try {
								BigInteger a= Converters.termToTime(n1,iX);
								try {
									BigInteger b= Converters.termToTime(n2,iX);
									if ( !operation.eval(a,b) ) {
										throw new Backtracking();
									}
								} catch (TermIsNotATime b1) {
									//:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
									//:: First argument: time, second argument: list.                    ::
									//:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
									try {
										Term head2= n2.getNextListHead(iX);
										Term tail2= n2.getNextListTail(iX);
										compare_item_and_list(iX,n1,operation,head2,tail2);
									} catch (EndOfList eol) {
										return;
									} catch (TermIsNotAList b2) {
										throw new WrongArgumentIsNotNumeric(n2);
									}
									//:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
								}
							} catch (TermIsNotATime a6) {
								//:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
								//:: First argument: list, second argument: list.                    ::
								//:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
								try {
									Term head1= n1.getNextListHead(iX);
									Term tail1= n1.getNextListTail(iX);
									try {
										Term head2= n2.getNextListHead(iX);
										compare_two_numbers(iX,head1,head2,operation);
										Term tail2= n2.getNextListTail(iX);
										while(true) {
											try {
												try {
													head1= tail1.getNextListHead(iX);
												} catch (TermIsNotAList a7) {
													throw new WrongArgumentIsNotAList(tail1);
												};
												try {
													head2= tail2.getNextListHead(iX);
												} catch (TermIsNotAList a7) {
													throw new WrongArgumentIsNotAList(tail2);
												};
												compare_two_numbers(iX,head1,head2,operation);
												try {
													tail1= tail1.getNextListTail(iX);
												} catch (TermIsNotAList a7) {
													throw new WrongArgumentIsNotAList(tail1);
												};
												try {
													tail2= tail2.getNextListTail(iX);
												} catch (TermIsNotAList a7) {
													throw new WrongArgumentIsNotAList(tail2);
												}
											} catch (EndOfList eol) {
												tail1= tail1.dereferenceValue(iX);
												tail2= tail2.dereferenceValue(iX);
												if (tail1.thisIsEmptyList() && tail2.thisIsEmptyList()) {
													break;
												} else {
													throw new VectorSizeMismatch();
												}
											// } catch (TermIsNotAList a7) {
											//	throw new WrongArgumentIsNotAList(tail1);
											}
										};
										return;
									} catch (EndOfList eol) {
										return;
									} catch (TermIsNotAList b1) {
										//:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
										//:: First argument: list, second argument: item.                    ::
										//:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
										compare_list_and_item(iX,head1,tail1,operation,n2);
										//:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
									}
								} catch (EndOfList eol) {
									return;
								} catch (TermIsNotAList a7) {
									throw new WrongArgumentIsNotNumeric(n1);
								}
								//:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
							}
						}
					}
				}
			}
		}
	}
	public static boolean realsAreEqual(double v, double value) {
		if (value==v) {
			return true;
		} else {
			long n= DefaultOptions.significantDigitsNumber;
			if (n > 0) {
				String fString= String.format("%%1.%de",n-1);
				String s1= String.format(fString,value);
				String s2= String.format(fString,v);
				if (s1.equals(s2)) {
					return true;
				}
			};
			return false;
		}
	}
	public static void compare_item_and_list(ChoisePoint iX, Term n1, ComparisonOperation operation, Term head2, Term tail2) throws Backtracking {
		compare_two_numbers(iX,n1,head2,operation);
		while(true) {
			try {
				head2= tail2.getNextListHead(iX);
				compare_two_numbers(iX,n1,head2,operation);
				tail2= tail2.getNextListTail(iX);
			} catch (EndOfList eol) {
				break;
			} catch (TermIsNotAList b) {
				throw new WrongArgumentIsNotAList(tail2);
			}
		}
	}
	public static void compare_list_and_item(ChoisePoint iX, Term head1, Term tail1, ComparisonOperation operation, Term n2) throws Backtracking {
		compare_two_numbers(iX,head1,n2,operation);
		while(true) {
			try {
				head1= tail1.getNextListHead(iX);
				compare_two_numbers(iX,head1,n2,operation);
				tail1= tail1.getNextListTail(iX);
			} catch (EndOfList eol) {
				break;
			} catch (TermIsNotAList b) {
				throw new WrongArgumentIsNotAList(tail1);
			}
		}
	}
	// Arithmetic operations
	public static void calculate_nullary_arithmetic_function(ChoisePoint iX, PrologVariable nV, NullaryArithmeticOperation operation) {
		nV.value= operation.eval();
		// iX.pushTrail(nV);
	}
	public static void calculate_unary_arithmetic_function(ChoisePoint iX, PrologVariable nV, Term n1, UnaryArithmeticOperation operation) {
		nV.value= calculate_unary_arithmetic_function(iX,n1,operation);
		// iX.pushTrail(nV);
	}
	public static Term calculate_unary_arithmetic_function(ChoisePoint iX, Term n1, UnaryArithmeticOperation operation) {
		try {
			BigInteger a= n1.getIntegerValue(iX);
			return operation.eval(a);
		} catch (TermIsNotAnInteger a1) {
			try {
				double a= n1.getRealValue(iX);
				return operation.eval(a);
			} catch (TermIsNotAReal a2) {
				try {
					Term head= n1.getNextListHead(iX);
					PrologList firstElement= new PrologList(
						calculate_unary_arithmetic_function(iX,head,operation),
						null);
					PrologList previousElement= firstElement;
					Term tail= n1.getNextListTail(iX);
					while(true) {
						try {
							head= tail.getNextListHead(iX);
							PrologList currentElement= new PrologList(
								calculate_unary_arithmetic_function(iX,head,operation),
								null);
							previousElement.tail= currentElement;
							previousElement= currentElement;
							tail= tail.getNextListTail(iX);
						} catch (EndOfList eol) {
							previousElement.tail= new PrologEmptyList();
							break;
						} catch (TermIsNotAList a3) {
							throw new WrongArgumentIsNotAList(tail);
						}
					};
					return firstElement;
				} catch (EndOfList eol) {
					return new PrologEmptyList();
				} catch (TermIsNotAList a3) {
					throw new WrongArgumentIsNotNumeric(n1);
				}
			}
		}
	}
	public static void calculate_binary_arithmetic_function(ChoisePoint iX, PrologVariable nV, Term n1, Term n2, BinaryArithmeticOperation operation) {
		nV.value= calculate_binary_arithmetic_function(iX,n1,n2,operation);
		// iX.pushTrail(nV);
	}
	public static Term calculate_binary_arithmetic_function(ChoisePoint iX, Term n1, Term n2, BinaryArithmeticOperation operation) {
		try {
			BigInteger a= n1.getIntegerValue(iX);
			try {
				BigInteger b= n2.getIntegerValue(iX);
				return operation.eval(a,b);
			} catch (TermIsNotAnInteger b1) {
				try {
					double b= n2.getRealValue(iX);
					double ra= a.doubleValue();
					return operation.eval(ra,b);
				} catch (TermIsNotAReal b2) {
					try {
						String sb= n2.getStringValue(iX);
						return operation.eval(a,sb);
					} catch (TermIsNotAString b3) {
						try {
							BigInteger b= Converters.termToDate(n2,iX);
							return operation.evalDate(a.multiply(Converters.oneDayLengthBigInteger),b);
						} catch (TermIsNotADate b4) {
							try {
								BigInteger b= Converters.termToTime(n2,iX);
								return operation.evalTime(a,b);
							} catch (TermIsNotATime b5) {
								//:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
								//:: First argument: integer, second argument: list.                 ::
								//:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
								try {
									Term head2= n2.getNextListHead(iX);
									Term tail2= n2.getNextListTail(iX);
									return calculate_binary_arithmetic_function(iX,n1,operation,head2,tail2);
								} catch (EndOfList eol) {
									return new PrologEmptyList();
								} catch (TermIsNotAList b6) {
									throw new WrongArgumentIsNotNumeric(n2);
								}
								//:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
							}
						}
					}
				}
			}
		} catch (TermIsNotAnInteger a1) {
			try {
				double a= n1.getRealValue(iX);
				try {
					BigInteger b= n2.getIntegerValue(iX);
					double rb= b.doubleValue();
					return operation.eval(a,rb);
				} catch (TermIsNotAnInteger b1) {
					try {
						double b= n2.getRealValue(iX);
						return operation.eval(a,b);
					} catch (TermIsNotAReal b2) {
						//:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
						//:: First argument: real, second argument: list.                    ::
						//:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
						try {
							Term head2= n2.getNextListHead(iX);
							Term tail2= n2.getNextListTail(iX);
							return calculate_binary_arithmetic_function(iX,n1,operation,head2,tail2);
						} catch (EndOfList eol) {
							return new PrologEmptyList();
						} catch (TermIsNotAList b3) {
							throw new WrongArgumentIsNotNumeric(n2);
						}
						//:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
					}
       	                        }
			} catch (TermIsNotAReal a2) {
				try {
					String sa= n1.getStringValue(iX);
					try {
						String sb= n2.getStringValue(iX);
						return operation.eval(sa,sb);
					} catch (TermIsNotAString b1) {
						try {
							BigInteger b= n2.getIntegerValue(iX);
							return operation.eval(sa,b);
						} catch (TermIsNotAnInteger b2) {
							//:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
							//:: First argument: string, second argument: list.                  ::
							//:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
							try {
								Term head2= n2.getNextListHead(iX);
								Term tail2= n2.getNextListTail(iX);
								return calculate_binary_arithmetic_function(iX,n1,operation,head2,tail2);
							} catch (EndOfList eol) {
								return new PrologEmptyList();
							} catch (TermIsNotAList b3) {
								throw new WrongArgumentIsNotNumeric(n2);
							}
							//:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
						}
					}
				} catch (TermIsNotAString a3) {
					try {
						BigInteger a= Converters.termToDate(n1,iX);
						try {
							BigInteger b= n2.getIntegerValue(iX);
							return operation.evalDate(a,b.multiply(Converters.oneDayLengthBigInteger));
						} catch (TermIsNotAnInteger b1) {
							try {
								BigInteger b= Converters.termToDate(n2,iX);
								return operation.evalDays(a,b);
							} catch (TermIsNotADate b2) {
								throw new WrongArgumentIsNotNumeric(n2);
							}
						}
					} catch (TermIsNotADate a4) {
						try {
							BigInteger a= Converters.termToTime(n1,iX);
							try {
								BigInteger b= n2.getIntegerValue(iX);
								return operation.evalTime(a,b);
							} catch (TermIsNotAnInteger b1) {
								try {
									BigInteger b= Converters.termToTime(n2,iX);
									return operation.eval(a,b);
								} catch (TermIsNotATime b2) {
									throw new WrongArgumentIsNotNumeric(n2);
								}
							}
						} catch (TermIsNotATime a5) {
							//:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
							//:: First argument: list, second argument: list.                    ::
							//:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
							try {
								Term head1= n1.getNextListHead(iX);
								Term tail1= n1.getNextListTail(iX);
								try {
									Term head2= n2.getNextListHead(iX);
									PrologList firstElement= new PrologList(
										calculate_binary_arithmetic_function(iX,head1,head2,operation),
										null);
									PrologList previousElement= firstElement;
									Term tail2= n2.getNextListTail(iX);
									while(true) {
										try {
											try {
												head1= tail1.getNextListHead(iX);
											} catch (TermIsNotAList a6) {
												throw new WrongArgumentIsNotAList(tail1);
											};
											try {
												head2= tail2.getNextListHead(iX);
											} catch (TermIsNotAList a6) {
												throw new WrongArgumentIsNotAList(tail2);
											};
											PrologList currentElement= new PrologList(
												calculate_binary_arithmetic_function(iX,head1,head2,operation),
												null);
											previousElement.tail= currentElement;
											previousElement= currentElement;
											try {
												tail1= tail1.getNextListTail(iX);
											} catch (TermIsNotAList a6) {
												throw new WrongArgumentIsNotAList(tail1);
											};
											try {
												tail2= tail2.getNextListTail(iX);
											} catch (TermIsNotAList a6) {
												throw new WrongArgumentIsNotAList(tail2);
											}
										} catch (EndOfList eol) {
											tail1= tail1.dereferenceValue(iX);
											tail2= tail2.dereferenceValue(iX);
											if (tail1.thisIsEmptyList() && tail2.thisIsEmptyList()) {
												previousElement.tail= new PrologEmptyList();
												break;
											} else {
												throw new VectorSizeMismatch();
											}
										// } catch (TermIsNotAList a6) {
										//	throw new WrongArgumentIsNotAList();
										}
									};
									return firstElement;
								} catch (EndOfList eol) {
									return new PrologEmptyList();
								} catch (TermIsNotAList b1) {
									//:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
									//:: First argument: list, second argument: item.                    ::
									//:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
									return calculate_binary_arithmetic_function(iX,head1,tail1,operation,n2);
									//:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
								}
							} catch (EndOfList eol) {
								return new PrologEmptyList();
							} catch (TermIsNotAList a6) {
								throw new WrongArgumentIsNotNumeric(n1);
							}
							//:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
						}
					}
				}
			}
		}
	}
	//
	public static Term calculate_binary_arithmetic_function(ChoisePoint iX, Term n1, BinaryArithmeticOperation operation, Term head2, Term tail2) {
		PrologList firstElement= new PrologList(
			calculate_binary_arithmetic_function(iX,n1,head2,operation),
			null);
		PrologList previousElement= firstElement;
		while(true) {
			try {
				head2= tail2.getNextListHead(iX);
				PrologList currentElement= new PrologList(
					calculate_binary_arithmetic_function(iX,n1,head2,operation),
					null);
				previousElement.tail= currentElement;
				previousElement= currentElement;
				tail2= tail2.getNextListTail(iX);
			} catch (EndOfList eol) {
				previousElement.tail= new PrologEmptyList();
				break;
			} catch (TermIsNotAList a4) {
				throw new WrongArgumentIsNotAList(tail2);
			}
		};
		return firstElement;
	}
	public static Term calculate_binary_arithmetic_function(ChoisePoint iX, Term head1, Term tail1, BinaryArithmeticOperation operation, Term n2) {
		PrologList firstElement= new PrologList(
			calculate_binary_arithmetic_function(iX,head1,n2,operation),
			null);
		PrologList previousElement= firstElement;
		while(true) {
			try {
				head1= tail1.getNextListHead(iX);
				PrologList currentElement= new PrologList(
					calculate_binary_arithmetic_function(iX,head1,n2,operation),
					null);
				previousElement.tail= currentElement;
				previousElement= currentElement;
				tail1= tail1.getNextListTail(iX);
			} catch (EndOfList eol) {
				previousElement.tail= new PrologEmptyList();
				break;
			} catch (TermIsNotAList a4) {
				throw new WrongArgumentIsNotAList(tail1);
			}
		};
		return firstElement;
	}
}
