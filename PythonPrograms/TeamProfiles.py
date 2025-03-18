import pandas as pd

def process_basketball_data(input_csv, teams_csv, output_excel):
    # Read the game data
    df = pd.read_csv(input_csv)
    
    # Read the team names
    teams_df = pd.read_csv(teams_csv)
    
    # Select relevant columns
    selected_columns = [
        "Season", "WTeamID", "WScore", "LTeamID", "LScore",
        "WFGM", "WFGA", "WFGM3", "WFGA3", "WFTM", "WFTA", "WOR", "WDR", "WAst", "WTO", "WStl", "WBlk", "WPF",
        "LFGM", "LFGA", "LFGM3", "LFGA3", "LFTM", "LFTA", "LOR", "LDR", "LAst", "LTO", "LStl", "LBlk", "LPF"
    ]
    df = df[selected_columns]
    
    # Create a new dataframe to store team profiles
    team_stats = []
    
    # Process winning team stats
    win_stats = df[["Season", "WTeamID", "WScore", "WFGM", "WFGA", "WFGM3", "WFGA3", "WFTM", "WFTA", "WOR", "WDR", "WAst", "WTO", "WStl", "WBlk", "WPF"]]
    win_stats = win_stats.rename(columns=lambda x: x.replace("W", ""))
    win_stats = win_stats.rename(columns={"TeamID": "TeamID", "Score": "Points"})
    team_stats.append(win_stats)
    
    # Process losing team stats
    lose_stats = df[["Season", "LTeamID", "LScore", "LFGM", "LFGA", "LFGM3", "LFGA3", "LFTM", "LFTA", "LOR", "LDR", "LAst", "LTO", "LStl", "LBlk", "LPF"]]
    lose_stats = lose_stats.rename(columns=lambda x: x.replace("L", ""))
    lose_stats = lose_stats.rename(columns={"TeamID": "TeamID", "Score": "Points"})
    team_stats.append(lose_stats)
    
    # Combine both datasets
    team_df = pd.concat(team_stats)
    
    # Group by Season and TeamID, then calculate the mean for all stats
    team_profiles = team_df.groupby(["Season", "TeamID"]).mean().reset_index()
    
    # Merge with team names
    team_profiles = team_profiles.merge(teams_df, on="TeamID", how="left")
    
    # Reorder columns to place Team Name first
    column_order = ["TeamName", "Season", "TeamID"] + [col for col in team_profiles.columns if col not in ["TeamName", "Season", "TeamID"]]
    team_profiles = team_profiles[column_order]
    
    # Save to Excel
    team_profiles.to_excel(output_excel, index=False)
    print(f"Processed team profiles with names saved to {output_excel}")

# Usage

input_file = "/Users/skyeb/desktop/NeuralNetwork/MRegularSeasonDetailedResults2.csv"
output_file = "/Users/skyeb/desktop/NeuralNetwork/team_profiles.xlsx"
teams_file = "/Users/skyeb/desktop/NeuralNetwork/MTeams2.csv"
process_basketball_data(input_file, teams_file, output_file)
