sentece_length <- read.table(file = "~/GitHub/BookTravel/backend/data/csv/sentence_length.csv", header = TRUE)
sentece_length <- subset(sentece_length, length < 1000)
hist(sentece_length$length, breaks = c(seq(0, 1000, 50)), main="Histogram for sentence length", xlab="Length in chars", col="green")