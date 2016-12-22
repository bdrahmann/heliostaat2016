// Functions for the Add-Ons from BHJ Drahmann
// Marked with BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB

void Display_Format(float MyFloat,int with, int dec, int col, int row) {	// function for formatting float numbers on Graphical Display
	dtostrf(MyFloat, with, dec, strbuffer);
	GLCD.DrawString(strbuffer, gTextfmt_col(col), gTextfmt_row(row));
}

/* Memory place for several variables
	latitude:  4091
	longitude: 4087
	timezone   4083
	useNorthAsZero: 4081
	updateEvery: 4077
	moveAwayFromLimit: 4073
	hourReset: 4071
	machineAltParkAngle: 4067
	machineAzParkAngle: 4063
	summertime: 4061

*/

