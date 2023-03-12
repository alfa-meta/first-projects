from selenium import webdriver
from selenium.webdriver.common.keys import Keys
import time

index_list = ['digit']
original_list = ['digit', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z' ]


driver1 = webdriver.Chrome(executable_path="C:\Program Files (x86)\chromedriver.exe")
driver2 = webdriver.Chrome(executable_path="C:\Program Files (x86)\chromedriver.exe")

driver_list = [driver1, driver2]

for driver in driver_list:
    driver.get("https://redacted/login")



    username = driver.find_element_by_xpath("//*[@id='username']")
    password = driver.find_element_by_name("password")

    username.send_keys("redacted")
    password.send_keys("redacted")
    driver.find_element_by_xpath('//*[@id="page"]/div[1]/div/div/div/div/form/div/div[2]/div[1]/div/button').click()

    #After loging in find the movie tab and the digit page(#)
    driver.get("https://redacted/movie")
    driver.get("https://redacted/movie_list/detail/index/digit")

#The actual movie
j = 0
x = 1
count = 1
overall_count = 0
show_more = driver1.find_element_by_xpath('/html/body/div[4]/div/div[3]/div[1]/div[2]/div/div/div/div[1]/div/div/div[3]/div/p[1]/a')
movie_xpath = driver1.find_element_by_xpath('/html/body/div[4]/div/div[3]/div[1]/div[2]/div/div/div/div[1]/div/div/div[2]/div[' + str(x) +']/div/div/div/div[1]/a')


for z in range(len(index_list)):
    file = open("redacted list-" + index_list[z] +".txt", "w")
    driver1.get("https://redacted/movie_list/detail/index/" + index_list[z])
    
    j = 0
    x = 1
    count = 0
    movie_xpath = driver1.find_element_by_xpath('/html/body/div[4]/div/div[3]/div[1]/div[2]/div/div/div/div[1]/div/div/div[2]/div[' + str(x) +']/div/div/div/div[1]/a')
    show_more = driver1.find_element_by_xpath('/html/body/div[4]/div/div[3]/div[1]/div[2]/div/div/div/div[1]/div/div/div[3]/div/p[1]/a')
    
    while show_more.is_displayed() == True:
        if show_more.is_displayed() == False:
            break
        show_more.click()
        j += 1
        time.sleep(2)
        try: show_more = driver1.find_element_by_xpath('/html/body/div[4]/div/div[3]/div[1]/div[2]/div/div/div/div[1]/div/div/div[2]/div['+ str((j * 12) + 13) +']/div/p[1]/a')
        except: break

    while movie_xpath.is_displayed() == True:
        url = driver1.find_element_by_xpath('/html/body/div[4]/div/div[3]/div[1]/div[2]/div/div/div/div[1]/div/div/div[2]/div[' + str(x) +']/div/div/div/div[1]/a').get_attribute("href")
        x += 1
        driver2.get(url)
        element = driver2.find_element_by_xpath("/html/body/div[4]/div/div[3]/div[1]/div[2]/div/div[1]/div/div[1]/div/div[3]/div/div[1]/a") #locates the film 5 minute preview button
        element.click()
        time.sleep(3) ## This is required to properly capture the url change. However wether the perfect timer is 0.5 seconds, 2 or 3 is unknown
        element.click()
        video_element = driver2.find_element_by_xpath("/html/body/div[4]/div/div[3]/div[1]/div[2]/div/div[1]/div/div[1]/div/div[2]/div/div[2]/div[4]/video").get_attribute("src")[0:-16] ## The link element has 16 worthless character to the link, so I will remove it
        video_title = driver2.title[0:-12] ## the title element has 12 worthless characters in this case - tvlinks.cc
        print(video_title)
        print(video_element)
        file.write('@' + str(video_element) + '\n' + str(video_title) + '\n')
        count += 1
        print(count)
        try: movie_xpath = driver1.find_element_by_xpath('/html/body/div[4]/div/div[3]/div[1]/div[2]/div/div/div/div[1]/div/div/div[2]/div[' + str(x) +']/div/div/div/div[1]/a')
        except: break

    overall_count += count
    file.close()


time.sleep(500)



### new xpath
"/html/body/div[4]/div/div[3]/div[1]/div[2]/div/div[1]/div/div[1]/div/div[2]/div/div[2]/div[4]/video"
"/html/body/div[4]/div/div[3]/div[1]/div[2]/div/div[1]/div/div[1]/div/div[2]/div/div[2]/div[4]/video"


'driver1.get("https://redacted/movie_list/detail/index/" + str(index_list[0]))'
'/html/body/div[4]/div/div[3]/div[1]/div[2]/div/div/div/div[1]/div/div/div[2]/div[2]/div/div/div/div[1]/a'
'/html/body/div[4]/div/div[3]/div[1]/div[2]/div/div/div/div[1]/div/div/div[2]/div[1]/div/div/div/div[1]/a'
'/html/body/div[4]/div/div[3]/div[1]/div[2]/div/div/div/div[1]/div/div/div[2]/div[3]/div/div/div/div[1]/a'


### xpath for link A
'/html/body/div[4]/div/div[3]/div[1]/div[2]/div/div/div/div[1]/div/div/div[2]/div[1]/div/div/div/div[1]/a'

### xpath for link B
'/html/body/div[4]/div/div[3]/div[1]/div[2]/div/div/div/div[1]/div/div/div[2]/div[1]/div/div/div/div[1]/a'

### xpath for show_more B
'/html/body/div[4]/div/div[3]/div[1]/div[2]/div/div/div/div[1]/div/div/div[3]/div/p[1]/a'

### xpath for show_more C
'/html/body/div[4]/div/div[3]/div[1]/div[2]/div/div/div/div[1]/div/div/div[3]/div/p[1]/a'
'/html/body/div[4]/div/div[3]/div[1]/div[2]/div/div/div/div[1]/div/div/div[2]/div[25]/div/p[1]/a'