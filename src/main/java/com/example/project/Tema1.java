package com.example.project;

import java.io.*;
import java.util.Scanner;

class score
{
	public score() {}
	public void submitquizz(String[] args) {
		user_passw userpass = new user_passw();
		user_passw user = userpass.verif_login(args);
		if (user == null) {
			return;
		}
		if (args.length == 3) {
			System.out.print("{'status':'error','message':'No quizz identifier was provided'}");
			return;
		}
		int id = 1;
		try {
			File file = new File("./src/main/java/com/example/project/questions.csv");
			Scanner reader = new Scanner(file);
			while (reader.hasNextLine()) {
				String data = reader.nextLine();
				String[] data_split = data.split(",");
				try (
						FileWriter fw = new FileWriter("./src/main/java/com/example/project/all.csv", true)
				) {
					fw.write(data_split[1]);
					fw.write(",");
					fw.write(data_split[0]);
					fw.write(",");

					try {
						File file1 = new File("./src/main/java/com/example/project/answers.csv");
						Scanner reader1 = new Scanner(file1);
						while (reader1.hasNextLine()) {
							String data1 = reader1.nextLine();
							String[] d1 = data1.split(",");
							for (int i = 0; i < d1.length; i++) {
								if (d1[d1.length - 1].equals(data_split[1])) {
									if (i != d1.length - 1) {
										if (!d1[i].equals("'1'") && !d1[i].equals("'0'")) {
											fw.write(Integer.toString(id));
											fw.write(",");
											id++;
										}
										fw.write(d1[i]);
										fw.write(",");
									}
								}
							}

						}
						fw.write("\n");
						reader1.close();
					} catch (IOException e) {
						System.out.println("An error occurred.");
						e.printStackTrace();
					}

				} catch (IOException e) {
					System.out.println("An error occurred.");
					e.printStackTrace();
				}
			}

		} catch (IOException e) {
			System.out.println("An error occurred.");
			e.printStackTrace();
		}

		String[] arg3 = args[3].split(" ");
		int p;// raspuns bun
		int k;// raspuns prost
		int corecte;
		int gresite;
		double procent = 0;
		double score = 0;
		if (arg3[0].equals("-quiz-id")) {
			try {
				File file = new File("./src/main/java/com/example/project/quiz.csv");
				Scanner reader = new Scanner(file);
				while (reader.hasNextLine()) {
					String data = reader.nextLine();
					String[] d = data.split(",");
					String[] argumentul3 = arg3[1].split("'");
					if (argumentul3[1].equals(d[1])) {
						for (int i = 2; i < d.length; i++) {
							try {
								File file1 = new File("./src/main/java/com/example/project/all.csv");
								Scanner reader1 = new Scanner(file1);
								while (reader1.hasNextLine()) {
									String data1 = reader1.nextLine();
									String[] d1 = data1.split(",");
									if ((d[i]).equals(d1[1])) {
										k = 0;
										p = 0;
										corecte = 0;
										gresite = 0;
										for (int s = 0; s < d1.length; s++) {
											if (d1[s].equals("'1'")) {
												corecte++;
											} else if (d1[s].equals("'0'")) {
												gresite++;
											}
										}
										for (int j = 4; j < args.length; j++) {
											String[] arg4 = args[j].split(" ");
											String[] argument4 = arg4[1].split("'");
											for (int l = 2; l < d1.length; l += 3) {
												if (argument4[1].equals(d1[l])) {
													if (d1[l + 2].equals("'1'")) {
														p++;
													} else if (d1[l + 2].equals("'0'")) {
														k++;
													}
												}
												procent = (double) (((100) / corecte) * p - ((100) / gresite) * k);
											}
										}
										if (procent != 0)
											procent = procent / (d.length - 2);
										score = score + procent;

									}
								}
							} catch (IOException e) {
								System.out.println("An error occurred.");
								e.printStackTrace();
							}
						}
						if (score - (int) score > 0.5)
							score = (int) score + 1;
						else
							score = (int) score;
						if (score < 0)
							score = 0;
						System.out.println("{'status':'ok','message':'" + (int) score + " points'}");
						try (
								FileWriter out = new FileWriter("./src/main/java/com/example/project/score.csv", true)
						) {
							out.write(d[1] + "," + (int) score + " ");
						} catch (IOException e) {
							System.out.println("An error occurred.");
							e.printStackTrace();
						}
						return;
					}
				}
				System.out.println("{'status':'error','message':'No quiz was found'}");
			} catch (IOException e) {
				System.out.println("An error occurred.");
				e.printStackTrace();
			}
		}
	}
	public void getsolutions(String[] args) {
		user_passw userpass = new user_passw();
		user_passw user = userpass.verif_login(args);
		if (user == null) {
			return;
		}
		try {
			File file = new File("./src/main/java/com/example/project/quiz.csv");
			Scanner reader = new Scanner(file);
			System.out.print("{'status':'ok','message':'[");
			while (reader.hasNextLine()) {
				String data = reader.nextLine();
				String[] d = data.split(",");
				String[] name = d[0].split(" ");
				System.out.print("{" + '"' + "quiz-id" + '"' + " : " + '"' + d[1] + '"' + ", " + '"' + "quiz-name" + '"' + " : " + '"' + name[0].replace("'", "") + " " + name[1].replace("'", "") + '"');
				try {
					File file2 = new File("./src/main/java/com/example/project/score.csv");
					Scanner reader2 = new Scanner(file2);
					while (reader2.hasNextLine()) {
						String data2 = reader2.nextLine();
						String[] d2 = data2.split(",");
						System.out.print(", " + '"' + "score" + '"' + " : " + '"' + d2[1].replace(" ", "") + '"' + ", " + '"' + "index_in_list" + '"' + " : " + '"' + d2[0] + '"' + "}");

					}
				} catch (IOException e) {
					System.out.println("An error occurred.");
					e.printStackTrace();
				}
			}
			System.out.print("]'}");
			reader.close();
			return;
		} catch (IOException e) {
			System.out.println("An error occurred.");
			e.printStackTrace();
		}
		System.out.print("{'status':'ok','message'");
	}
}

class quizz
{
	public quizz() {}
	public void createquiz(String[] args) {
		user_passw userpass = new user_passw();
		user_passw user = userpass.verif_login(args);
		if(user == null)
		{
			return;
		}
		String name = args[3];
		String[] actual_name = name.split(" ");
		String nume = "";
		for(int i = 1; i < actual_name.length; i++)
		{
			nume = nume + actual_name[i] + " ";
		}
		if(actual_name[0].equals("-name"))
		{
			int id = 1;
			try {
				File file = new File("./src/main/java/com/example/project/quiz.csv");
				Scanner reader = new Scanner(file);
				while (reader.hasNextLine()) {
					id++;
					String data = reader.nextLine();
					String[] data_split = data.split(",");
					if(data_split[0].equals(nume))
					{
						System.out.print("{'status':'error','message':'Quizz name already exists'}");
						reader.close();
						return;
					}
				}
				reader.close();
			}
			catch (IOException e) {
				System.out.println("An error occurred.");
				e.printStackTrace();
			}
			try (
					FileWriter myWriter = new FileWriter("./src/main/java/com/example/project/quiz.csv", true)
			) {
				for(int i = 1; i < actual_name.length; i++)
				{
					myWriter.write(actual_name[i] + " ");
				}
				myWriter.write(",");
				myWriter.write((Integer.toString(id)));
			}
			catch (IOException e) {
				System.out.println("An error occurred.");
				e.printStackTrace();
			}
		}
		else
		{
			System.out.print("{'status':'error','message':'No question text provided'}");
			return;
		}
		for (int i = 0; args.length > 3 + i + 1; i++) {
			String[] q = args[4 + i].split(" ");
			int count = i+1;
			if (q[0].equals("-question-" + count)) {
				String question = q[1];
				String[] ques = question.split("'");
				try {
					File file = new File("./src/main/java/com/example/project/questions.csv");
					Scanner reader = new Scanner(file);
					int ok = 0;
					while (reader.hasNextLine()) {
						String data = reader.nextLine();
						String[] data_split = data.split(",");
						if (!data_split[0].equals("")) {
							if (data_split[1].equals(ques[1])) {
								try (
										FileWriter myWriter = new FileWriter("./src/main/java/com/example/project/quiz.csv", true)
								) {
									myWriter.write(",");
									myWriter.write(data_split[0]);
									ok = 1;
								} catch (IOException e) {
									System.out.println("An error occurred.");
									e.printStackTrace();
								}

							}
							if (ok == 0 && !reader.hasNextLine()) {
								System.out.print("{'status':'error','message':'Question ID for question " + ques[1] + " does not exist'}");
								reader.close();
								return;
							}
						}

					}
					reader.close();
				}
				catch (IOException e) {
					System.out.println("An error occurred.");
					e.printStackTrace();
				}
			}
		}
		try (
				FileWriter myWriter = new FileWriter("./src/main/java/com/example/project/quiz.csv", true)
		) {
			myWriter.write("\n");
		} catch (IOException e) {
			System.out.println("An error occurred.");
			e.printStackTrace();
		}
		if(args.length >  14)
		{
			System.out.println("{'status':'error','message':'Quizz has more than 10 questions'}");
			return;
		}
		System.out.println("{'status':'ok','message':'Quizz added succesfully'}");
	}
	public void getquizzbyname(String[] args) {
		user_passw userpass = new user_passw();
		user_passw user = userpass.verif_login(args);
		if(user == null)
		{
			return;
		}
		String name = args[3];
		String[] actual_name = name.split(" ");
		String nume = "";
		for(int i = 1; i < actual_name.length; i++)
		{
			nume = nume + actual_name[i] + " ";
		}
		if(actual_name[0].equals("-name"))
		{
			int id = 1;
			try {
				File file = new File("./src/main/java/com/example/project/quiz.csv");
				Scanner reader = new Scanner(file);
				if (!reader.hasNextLine()) {
					System.out.print("{'status':'error','message':'Quizz does not exist'}");
					reader.close();
				} else {
					while (reader.hasNextLine()) {
						String data = reader.nextLine();
						String[] data_split = data.split(",");
						if (data_split[0].equals(nume)) {
							System.out.print("{'status':'ok','message':'" + id + "'}");
							reader.close();
							return;
						} else {
							System.out.print("{'status':'error','message':'Quizz does not exist'}");
						}
						id++;
					}
					reader.close();
				}
			}
			catch(IOException e){
				System.out.println("An error occurred.");
				e.printStackTrace();
			}
		}
	}
	public void getallquizzes(String[] args) {
		user_passw userpass = new user_passw();
		user_passw user = userpass.verif_login(args);
		if (user == null) {
			return;
		}
		int val = 0;//valoare de adevar
		try {
			File file = new File("./src/main/java/com/example/project/quiz.csv");
			Scanner reader = new Scanner(file);
			while (reader.hasNextLine()) {
				String data = reader.nextLine();
				String[] data_split = data.split(",");
				if (data_split.length > 12)
					val = 1;
			}
			reader.close();
		} catch (IOException e) {
			System.out.println("An error occurred.");
			e.printStackTrace();
		}
		System.out.print("{'status':'ok','message':'[");
		try {
			File file = new File("./src/main/java/com/example/project/quiz.csv");
			Scanner reader = new Scanner(file);
			while (reader.hasNextLine()) {
				String data = reader.nextLine();
				String[] d = data.split(",");
				String[] actual_data = d[0].split("'");
				if (val == 0)
					System.out.print("{" + '"' + "quizz_id" + '"' + " : " + '"' + d[1] + '"' + ", " + '"' + "quizz_name" + '"' + " : " + '"' + actual_data[1] + '"' + ", " + '"' + "is_completed" + '"' + " : " + '"' + "False" + '"' + "}");
				else
					System.out.print("{" + '"' + "quizz_id" + '"' + " : " + '"' + d[1] + '"' + ", " + '"' + "quizz_name" + '"' + " : " + '"' + actual_data[1] + '"' + ", " + '"' + "is_completed" + '"' + " : " + '"' + "True" + '"' + "}");
				if (reader.hasNextLine())
					System.out.print(", ");
			}
			reader.close();
		} catch (IOException e) {
			System.out.println("An error occurred.");
			e.printStackTrace();
		}
		System.out.print("]'}");
	}
	public void getquizzdetails(String[] args) {
		user_passw userpass = new user_passw();
		user_passw user = userpass.verif_login(args);
		if (user == null) {
			return;
		}
		System.out.print("{'status':'ok','message':'[");
		try {
			File file = new File("./src/main/java/com/example/project/questions.csv");
			Scanner reader = new Scanner(file);
			while (reader.hasNextLine()) {
				String data = reader.nextLine();
				String[] d = data.split(",");
				String[] t1 = d[0].split("'");//nume ajutatoare pentru a scapa de semnele de punctuatie
				System.out.print("{" + '"' + "question-name" + '"' + ":" + '"' + t1[1] + '"' + ", " + '"' + "question_index" + '"' + ":" + '"' + d[1] + '"' + ", ");
				try {
					File file1 = new File("./src/main/java/com/example/project/type.csv");
					Scanner reader1 = new Scanner(file1);
					while (reader1.hasNextLine()) {
						String data1 = reader1.nextLine();
						String[] d1 = data1.split(",");
						String[] t2 = d1[1].split("'");//nume ajutatoare pentru a scapa de semnele de punctuatie
						if (d[0].equals(d1[0]))
							System.out.print('"' + "question_type" + '"' + ":" + '"' + t2[1] + '"' + ", ");
					}
				} catch (IOException e) {
					System.out.println("An error occurred.");
					e.printStackTrace();
				}
				System.out.print('"' + "answers" + '"' + ":" + '"' + "[");
				int k = 0;
				try {
					File file2 = new File("./src/main/java/com/example/project/answers.csv");
					Scanner reader2 = new Scanner(file2);
					while (reader2.hasNextLine()) {
						String data2 = reader2.nextLine();
						String[] d2 = data2.split(",");//nume ajutatoare pentru a scapa de semnele de punctuatie
						for (int i = 0; i < d2.length - 1; i += 2) {
							k++;
							if (d2[d2.length - 1].equals(d[1]))
								System.out.print("{" + '"' + "answer_name" + '"' + ":" + '"' + d2[i].replace("'", "") + '"' + ", " + '"' + "answer_id" + '"' + ":" + '"' + k + '"' + "}");
							if (d2[d2.length - 1].equals(d[1]) && i < d2.length - 3)
								System.out.print(", ");
						}
					}
					System.out.print("]" + '"' + "}");
					if (reader.hasNextLine())
						System.out.print(", ");
				} catch (IOException e) {
					System.out.println("An error occurred.");
					e.printStackTrace();
				}
			}
			System.out.print("]'}");
		} catch (IOException e) {
			System.out.println("An error occurred.");
			e.printStackTrace();
		}
	}
	public void deletequizz(String[] args) {
		user_passw userpass = new user_passw();
		user_passw user = userpass.verif_login(args);
		if (user == null) {
			return;
		}
		if (args.length == 3) {
			System.out.print("{'status':'error','message':'No quizz identifier was provided'}");
			return;
		}
		String[] arg3 = args[3].split(" ");
		if (arg3[0].equals("-id")) {
			try {
				File file = new File("./src/main/java/com/example/project/quiz.csv");
				Scanner reader = new Scanner(file);
				while (reader.hasNextLine()) {
					String data = reader.nextLine();
					String[] d = data.split(",");
					String[] id = arg3[1].split("'");
					if (d[1].equals(id[1])) {

						File inputFile = new File("./src/main/java/com/example/project/quiz.csv");
						File tempFile = new File("./src/main/java/com/example/project/quiz.csv");
						BufferedReader reader1 = new BufferedReader(new FileReader(inputFile));
						BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));

						String currentLine;
						while ((currentLine = reader1.readLine()) != null) {
							String trimmedLine = currentLine.trim();
							if (trimmedLine.equals(data)) continue;
							writer.write(currentLine + System.getProperty("line.separator"));
						}
						writer.close();
						reader1.close();
						System.out.print("{'status':'ok','message':'Quizz deleted successfully'}");
						reader.close();
						return;
					}
				}
				System.out.print("{'status':'error','message':'No quiz was found'}");
				reader.close();
			} catch (IOException e) {
				System.out.println("An error occurred.");
				e.printStackTrace();
			}
		}
	}

}

class questions
{
	public questions() {}
	public void createquestions(String[] args) {
		user_passw userpass = new user_passw();
		user_passw user = userpass.verif_login(args);
		if(user == null)
		{
			return;
		}
		String arg_text = args[3];
		String[] text = arg_text.split(" ");
		String textul = "";
		for(int i = 1; i < text.length; i++)
		{
			textul = textul + text[i] + " ";
		}
		int id = 1;
		if(text[0].equals("-text"))
		{
			try {
				File file = new File("./src/main/java/com/example/project/questions.csv");
				Scanner reader = new Scanner(file);
				while (reader.hasNextLine()) {
					id++;
					String data = reader.nextLine();
					String[] data_split = data.split(",");
					if(data_split[0].equals(textul))
					{
						System.out.print("{'status':'error','message':'Question already exists'}");
						reader.close();
						return;
					}
				}
				reader.close();
			}
			catch (IOException e) {
				System.out.println("An error occurred.");
				e.printStackTrace();
			}
			try (
					FileWriter myWriter = new FileWriter("./src/main/java/com/example/project/questions.csv", true)
			) {
				for(int i = 1; i < text.length; i++)
				{
					myWriter.write(text[i] + " ");
				}
				myWriter.write(",");
				myWriter.write((Integer.toString(id)));
				myWriter.write("\n");
			}
			catch (IOException e) {
				System.out.println("An error occurred.");
				e.printStackTrace();
			}
		}
		else
		{
			System.out.print("{'status':'error','message':'No question text provided'}");
		}
		if(args.length > 4)
		{
			String type = args[4];
			String[] ty = type.split(" ");
			if(ty[0].equals("-type"))
			{
				try (
						FileWriter myWriter = new FileWriter("./src/main/java/com/example/project/type.csv", true)
				) {
					for(int i = 1; i < text.length; i++)
					{
						myWriter.write(text[i] + " ");
					}
					myWriter.write(",");
					myWriter.write(ty[1]);
					myWriter.write(",");
				}
				catch (IOException e) {
					System.out.println("An error occurred.");
					e.printStackTrace();
				}
				try (
						FileWriter myWriter = new FileWriter("./src/main/java/com/example/project/type.csv", true)
				) {
					myWriter.write("\n");
				}
				catch (IOException e) {
					System.out.println("An error occurred.");
					e.printStackTrace();
				}
				if(ty[1].equals("'single'")) {
					int j = 1;//nr raspunsuri
					int k = 1;//nr raspunsuri verificate
					if(args.length == 7)
					{
						System.out.print("{'status':'error','message':'Only one answer provided'}");
						return;
					}
					if(args.length == 5)
					{
						System.out.print("{'status':'error','message':'No answer provided'}");
						return;
					}
					if(args.length > 14)
					{
						System.out.print("{'status':'error','message':'More than 5 answers were submitted'}");
						return;
					}
					for (int i = 0; args.length >= 5 + i + 1; i += 2) {

						String answer = args[5 + i];
						String[] actual_answer = answer.split(" ");
						if (actual_answer[0].equals("-answer-" + j)) {
							try {
								File file = new File("./src/main/java/com/example/project/answers.csv");
								Scanner reader = new Scanner(file);
								String data = "";
								while (reader.hasNextLine()) {
									data = reader.nextLine();
								}
								if (!data.equals("") && i != 0) {
									String[] data_split = data.split(",");
									for (int q = 0; q < data_split.length-1; q += 2) {
										if (data_split[q].equals(actual_answer[1])) {
											System.out.print("{'status':'error','message':'Same answer provided more than once'}");
											reader.close();
											return;
										}
									}
								}
								reader.close();
							} catch(IOException e){
								System.out.println("An error occurred.");
								e.printStackTrace();
							}
							try (
									FileWriter myWriter = new FileWriter("./src/main/java/com/example/project/answers.csv", true)
							) {
								myWriter.write(actual_answer[1]);
								myWriter.write(",");
							} catch (IOException e) {
								System.out.println("An error occurred.");
								e.printStackTrace();
							}
							j++;
						} else {
							System.out.print("{'status':'error','message':'Answer " + j + " has no answer description'}");
							return;
						}
						if(args.length < 6+ i +1)
						{
							System.out.print("{'status':'error','message':'Answer " + k + " has no answer correct flag'}");
							return;
						}
						String corect = args[6 + i];
						String[] corect_ver = corect.split(" ");
						if (corect_ver[0].equals("-answer-" + k + "-is-correct")) {
							try (
									FileWriter myWriter = new FileWriter("./src/main/java/com/example/project/answers.csv", true)
							) {
								myWriter.write(corect_ver[1]);
								myWriter.write(",");
							} catch (IOException e) {
								System.out.println("An error occurred.");
								e.printStackTrace();
							}
						}
						else
						{
							System.out.print("{'status':'error','message':'Answer " + k + " has no answer correct flag'}");
							return;
						}
						k++;
					}
					try {
						File file = new File("./src/main/java/com/example/project/answers.csv");
						Scanner reader = new Scanner(file);
						int count = 0;
						String data = "";
						while (reader.hasNextLine()) {
							data = reader.nextLine();
						}
						if (!data.equals("")) {
							String[] data_split = data.split(",");
							for (int i = 0; i < data_split.length-1; i+=2) {
								if (data_split[i + 1].equals("'1'")) {
									count++;
								}

								if (count > 1) {
									System.out.print("{'status':'error','message':'Single correct answer question has more than one correct answer'}");
									reader.close();
									return;
								}
							}
						}
						reader.close();
					} catch(IOException e){
						System.out.println("An error occurred.");
						e.printStackTrace();
					}
					try (
							FileWriter myWriter = new FileWriter("./src/main/java/com/example/project/answers.csv", true)
					) {
						myWriter.write(Integer.toString(id));
						myWriter.write("\n");
					} catch (IOException e) {
						System.out.println("An error occurred.");
						e.printStackTrace();
					}
					System.out.print("{'status':'ok','message':'Question added successfully'}");
				}
				else if(ty[1].equals("'multiple'")) {
					int j = 1;//nr raspunsuri
					int k = 1;//nr raspunsuri verificate
					if (args.length == 6) {
						System.out.print("{'status':'error','message':'Only one answer provided'}");
						return;
					}
					if (args.length == 5) {
						System.out.print("{'status':'error','message':'No answer provided'}");
						return;
					}
					if (args.length > 14) {
						System.out.print("{'status':'error','message':'More than 5 answers were submitted'}");
						return;
					}
					for (int i = 0; args.length >= 5 + i + 1; i += 2) {

						String answer = args[5 + i];
						String[] actual_answer = answer.split(" ");
						if (actual_answer[0].equals("-answer-" + j)) {
							try (
									FileWriter myWriter = new FileWriter("./src/main/java/com/example/project/answers.csv", true)
							) {
								myWriter.write(actual_answer[1]);
								myWriter.write(",");
							} catch (IOException e) {
								System.out.println("An error occurred.");
								e.printStackTrace();
							}
							j++;
						} else {
							System.out.print("{'status':'error','message':'Answer " + j + " has no answer description'}");
							return;
						}
						if (args.length < 6 + i + 1) {
							System.out.print("{'status':'error','message':'Answer " + k + " has no answer correct flag'}");
							return;
						}
						String corect = args[6 + i];
						String[] corect_ver = corect.split(" ");
						if (corect_ver[0].equals("-answer-" + k + "-is-correct")) {
							try (
									FileWriter myWriter = new FileWriter("./src/main/java/com/example/project/answers.csv", true)
							) {
								myWriter.write(corect_ver[1]);
								myWriter.write(",");
							} catch (IOException e) {
								System.out.println("An error occurred.");
								e.printStackTrace();
							}
						} else {
							System.out.print("{'status':'error','message':'Answer " + k + " has no answer correct flag'}");
							return;
						}
						k++;
					}
					try {
						File file = new File("./src/main/java/com/example/project/answers.csv");
						Scanner reader = new Scanner(file);
						String datarez = reader.nextLine();
						String[] drez = datarez.split(",");
						String rez = drez[0];
						String data = "";
						while (reader.hasNextLine()) {
							data = reader.nextLine();
						}
						if(data.equals("")) {
							String[] d = data.split(",");
							for (int i = 0; i < d.length-1; i += 2) {
								if (d[i].equals(rez)) {
									System.out.print("{'status':'error','message':'Same answer provided more than once'}");
									reader.close();
									return;
								}
							}
						}
						reader.close();
					} catch (IOException e) {
						System.out.println("An error occurred.");
						e.printStackTrace();
					}
					try (
							FileWriter myWriter = new FileWriter("./src/main/java/com/example/project/answers.csv", true)
					) {
						myWriter.write(Integer.toString(id));
						myWriter.write("\n");
					} catch (IOException e) {
						System.out.println("An error occurred.");
						e.printStackTrace();
					}
					System.out.print("{'status':'ok','message':'Question added successfully'}");
				}
			}
		}
	}
	public void getquestionid(String[] args, String anObject, String pathname, String s) {
		user_passw userpass = new user_passw();
		user_passw user = userpass.verif_login(args);
		if(user == null)
		{
			return;
		}
		String text = args[3];
		String[] actual_text = text.split(" ");
		String textul = "";
		for(int i = 1; i < actual_text.length; i++)
		{
			textul = textul + actual_text[i] + " ";
		}
		if(actual_text[0].equals(anObject))
		{
			int id = 1;
			try {
				File file = new File(pathname);
				Scanner reader = new Scanner(file);
				if (!reader.hasNextLine()) {
					System.out.print(s);
					reader.close();
				} else {
					while (reader.hasNextLine()) {
						String data = reader.nextLine();
						String[] data_split = data.split(",");
						if (data_split[0].equals(textul)) {
							System.out.print("{'status':'ok','message':'" + id + "'}");
							reader.close();
							return;
						} else {
							System.out.print(s);
						}
						id++;
					}
					reader.close();
				}
			}
			catch(IOException e){
				System.out.println("An error occurred.");
				e.printStackTrace();
			}
		}
	}
	public void getallquestions(String[] args) {
		user_passw userpass = new user_passw();
		user_passw user = userpass.verif_login(args);
		if(user == null)
		{
			return;
		}
		System.out.print("{'status':'ok','message':'[");
		try {
			File file = new File("./src/main/java/com/example/project/questions.csv");
			Scanner reader = new Scanner(file);
			while(reader.hasNextLine())
			{
				String data = reader.nextLine();
				String[] d = data.split(",");
				String[] actual_data = d[0].split("'");
				System.out.print("{"+'"' + "question_id" + '"' + " : " + '"' + d[1] + '"' + ", " + '"' + "question_name" + '"' + " : " + '"' + actual_data[1] + '"' + "}");
				if(reader.hasNextLine())
				{
					System.out.print(", ");
				}
			}
			reader.close();
		}
		catch(IOException e){
			System.out.println("An error occurred.");
			e.printStackTrace();
		}
		System.out.print("]'" + "}");
	}
}

class user_passw
{
	String username;
	String password;
	public user_passw() {}
	public user_passw(String username, String password)
	{
		this.username = username;
		this.password = password;
	}
	public user_passw verif_login(String[] args) {
		if(args.length == 1)
		{
			System.out.print("{'status':'error','message':'You need to be authenticated'}");
			return null;
		}
		else if(args.length == 2)
		{
			System.out.print("{'status':'error','message':'You need to be authenticated'}");
			return null;
		}

		String[] argument= args[1].split(" ");
		String u1 = argument[0];//u
		if(!u1.equals("-u"))
		{
			System.out.print("{'status':'error','message':'You need to be authenticated'}");
			return null;
		}
		else
		{
			try {
				File file = new File("./src/main/java/com/example/project/users.csv");
				Scanner reader = new Scanner(file);
				if(!reader.hasNextLine())
				{
					System.out.print("{'status':'error','message':'Login failed'}");
					reader.close();
					return null;
				}
				reader.close();
			}
			catch (IOException e) {
				System.out.println("An error occurred.");
				e.printStackTrace();
			}
		}
		String[] argument2=args[2].split(" ");
		if(!argument2[0].equals("-p"))
		{
			System.out.print("{'status':'error','message':'You need to be authenticated'}");
			return null;
		}
		else {
			try {
				File file = new File("./src/main/java/com/example/project/users.csv");
				Scanner reader = new Scanner(file);
				while (reader.hasNextLine()) {
					String data = reader.nextLine();
					String[] d = data.split(",");
					if (d[0].equals(argument[1])) {
						if (!d[1].equals(argument2[1])) {
							System.out.print("{'status':'error','message':'Login failed'}");
							reader.close();
							return null;
						}
					}
				}
				reader.close();
			} catch (IOException e) {
				System.out.println("An error occurred.");
				e.printStackTrace();
			}
		}
		return new user_passw(argument[1],argument2[1]);
	}
}

public class Tema1 {

	public static void main(final String[] args) {
		if (args == null) {
			System.out.print("Hello world!");
		} else {
			if (args[0].equals("-cleanup-all")) {
				try {
					PrintWriter writer = new PrintWriter("./src/main/java/com/example/project/users.csv");
					writer.print("");
					writer.close();
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
				try {
					PrintWriter writer = new PrintWriter("./src/main/java/com/example/project/questions.csv");
					writer.print("");
					writer.close();
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
				try {
					PrintWriter writer = new PrintWriter("./src/main/java/com/example/project/answers.csv");
					writer.print("");
					writer.close();
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
				try {
					PrintWriter writer = new PrintWriter("./src/main/java/com/example/project/quiz.csv");
					writer.print("");
					writer.close();
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
				try {
					PrintWriter writer = new PrintWriter("./src/main/java/com/example/project/type.csv");
					writer.print("");
					writer.close();
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
				try {
					PrintWriter writer = new PrintWriter("./src/main/java/com/example/project/all.csv");
					writer.print("");
					writer.close();
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
				try {
					PrintWriter writer = new PrintWriter("./src/main/java/com/example/project/score.csv");
					writer.print("");
					writer.close();
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
				System.out.print("{'status':'ok','message':'Cleanup finished successfully'}");
			} else if (args[0].equals("-create-user")) {
				if (args.length == 1) {
					System.out.print("{'status':'error','message':'Please provide username'}");
					return;
				} else if (args.length == 2) {
					System.out.print("{'status':'error','message':'Please provide password'}");
					return;
				}
				String[] argument = args[1].split(" ");
				String argument1 = argument[0];
				String username = argument[1];
				if (argument1.equals("-u")) {
					try {
						File file = new File("./src/main/java/com/example/project/users.csv");
						Scanner reader = new Scanner(file);
						while (reader.hasNextLine()) {
							String data = reader.nextLine();
							String[] d = data.split(",");
							if (d[0].equals(argument[1])) {
								System.out.print("{'status':'error','message':'User already exists'}");
								reader.close();
								return;
							}
						}
						reader.close();
					} catch (IOException e) {
						System.out.println("An error occurred.");
						e.printStackTrace();
					}
				} else {
					System.out.print("{'status':'error','message':'Please provide username'}");
					return;
				}
				String[] argument2 = args[2].split(" ");
				if (argument2[0].equals("-p")) {
					String my_password = argument2[1];
					System.out.print("{'status':'ok','message':'User created successfully'}");
					try (
							FileWriter myWriter = new FileWriter("./src/main/java/com/example/project/users.csv", true)
					) {
						myWriter.write(username);
						myWriter.write(",");
						myWriter.write(my_password);
						myWriter.write("\n");
					} catch (IOException e) {
						System.out.println("An error occurred.");
						e.printStackTrace();
					}
				} else {
					System.out.print("{'status':'error','message':'Please provide password'}");
				}
			} else if (args[0].equals("-create-question")) {
				questions q = new questions();
				q.createquestions(args);
			} else if (args[0].equals("-get-question-id-by-text")) {
				questions q = new questions();
				q.getquestionid(args, "-text", "./src/main/java/com/example/project/questions.csv", "{'status':'error','message':'Question does not exist'}");
			} else if (args[0].equals("-get-all-questions")) {
				questions q = new questions();
				q.getallquestions(args);
			} else if (args[0].equals("-create-quizz")) {
				quizz q = new quizz();
				q.createquiz(args);
			} else if (args[0].equals("-get-quizz-by-name")) {
				quizz q = new quizz();
				q.getquizzbyname(args);
			} else if (args[0].equals("-get-all-quizzes")) {
				quizz q = new quizz();
				q.getallquizzes(args);
			} else if (args[0].equals("-get-quizz-details-by-id")) {
				quizz q = new quizz();
				q.getquizzdetails(args);
			} else if (args[0].equals("-submit-quizz")) {
				score s = new score();
				s.submitquizz(args);
			} else if (args[0].equals("-delete-quizz-by-id")) {
				quizz q = new quizz();
				q.deletequizz(args);
			} else if (args[0].equals("-get-my-solutions")) {
				score s = new score();
				s.getsolutions(args);
			}
		}

	}
}
