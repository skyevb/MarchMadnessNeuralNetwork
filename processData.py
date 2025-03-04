import pandas as pd

def process_basketball_data(input_csv, output_excel):
    # Read the CSV file
    df = pd.read_csv(input_csv)
    
    # Select relevant columns
    selected_columns = [
        "Season", "WTeamID", "WScore", "LTeamID", "LScore",
        "WFGM", "WFGA", "WFGM3", "WFGA3", "WFTM", "WFTA", "WOR", "WDR", "WAst", "WTO", "WStl", "WBlk", "WPF",
        "LFGM", "LFGA", "LFGM3", "LFGA3", "LFTM", "LFTA", "LOR", "LDR", "LAst", "LTO", "LStl", "LBlk", "LPF"
    ]
    df = df[selected_columns]
    
    # Save to Excel
    df.to_excel(output_excel, index=False)
    print(f"Processed data saved to {output_excel}")

# Example usage
input_file = "/Users/skyeb/desktop/NeuralNetwork/MRegularSeasonDetailedResults2.csv"
output_file = "/Users/skyeb/desktop/NeuralNetwork/Basketball_Data.xlsx"
process_basketball_data(input_file, output_file)
