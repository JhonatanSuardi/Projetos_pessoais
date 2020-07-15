import org.junit.*;
import org.junit.runners.MethodSorters;
import org.openqa.selenium.By;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.Random;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class CadastroBarriga {

    private static String email = "";
    private ChromeDriver driver;
    private WebDriverWait wait;
    private String confirmaUsu;
    private Random random;
    private int rdm;

    @Before
    public void acessaUrl() {
        System.getProperty("webdriver.chrome.driver", "chromedriver.exe");
        this.driver = new ChromeDriver();
        wait = new WebDriverWait(driver, 90);
        this.driver.get("https://srbarriga.herokuapp.com/login");
        this.driver.manage().window().fullscreen();
    }

    @Test
    public void aCriaUsuario() {
        this.random = new Random();
        rdm = random.nextInt()*100;
        setEmail("maluquinho" + rdm + ".maluco@gmail.com");
        this.driver.findElement(By.xpath("//a[@href='/cadastro']")).click();
        this.wait.until(ExpectedConditions.visibilityOf(this.driver.findElement(By.id("nome"))));
        this.driver.findElement(By.id("nome")).sendKeys("Jhonatan");
        this.driver.findElement(By.id("email")).sendKeys(getEmail());
        this.driver.findElement(By.id("senha")).sendKeys("4321");
        this.driver.findElement(By.xpath("//input[@class='btn btn-primary']")).click();
        this.wait.until(ExpectedConditions.visibilityOf(this.driver.findElement(By.xpath("//div[@role='alert']"))));
        confirmaUsu = this.driver.findElement(By.xpath("//div[@role='alert']")).getText();
        Assert.assertEquals(confirmaUsu, "Usuário inserido com sucesso");
        System.out.println("RDM: "+ rdm);
        System.out.println(getEmail());
    }

    @Test
    public void bValidaLogin(){
        System.out.println(getEmail());
        System.out.println("RDM: "+ rdm);
        this.driver.findElement(By.id("email")).sendKeys(getEmail());
        this.driver.findElement(By.id("senha")).sendKeys("4321");
        this.driver.findElement(By.xpath("//button[@type='submit']")).click();

        this.wait.until(ExpectedConditions.visibilityOf(this.driver.findElement(By.xpath("//div[@role='alert']"))));
        confirmaUsu = this.driver.findElement(By.xpath("//div[@role='alert']")).getText();

        Assert.assertEquals("Bem vindo, Jhonatan!", confirmaUsu);
    }

    @Test
    public void cCriarConta(){
        bValidaLogin();
        this.driver.findElement(By.xpath("//a[@role='button']")).click();
        this.driver.findElement(By.xpath("//a[@href='/addConta']")).click();
        this.driver.findElement(By.id("nome")).sendKeys("Jhowzin");
        this.driver.findElement(By.xpath("//button[@type='submit']")).click();

        this.wait.until(ExpectedConditions.visibilityOf(this.driver.findElement(By.xpath("//div[@role='alert']"))));
        confirmaUsu = this.driver.findElement(By.xpath("//div[@role='alert']")).getText();

        Assert.assertEquals("Conta adicionada com sucesso!", confirmaUsu);
    }

    @Test
    public void dCriarMovimentacao(){
        bValidaLogin();
        this.driver.findElement(By.xpath("//a[@href='/movimentacao']")).click();
        this.driver.findElement(By.id("data_transacao")).sendKeys("26/03/2020");
        this.driver.findElement(By.id("data_pagamento")).sendKeys("27/03/2020");
        this.driver.findElement(By.id("descricao")).sendKeys("pagamento");
        this.driver.findElement(By.id("interessado")).sendKeys("Joaozin");
        this.driver.findElement(By.id("valor")).sendKeys("600");
        this.driver.findElement(By.id("status_pago")).click();
        this.driver.findElement(By.xpath("//button[@type='submit']")).click();

        this.wait.until(ExpectedConditions.visibilityOf(this.driver.findElement(By.xpath("//div[@role='alert']"))));
        confirmaUsu = this.driver.findElement(By.xpath("//div[@role='alert']")).getText();

        Assert.assertEquals("Movimentação adicionada com sucesso!", confirmaUsu);

    }

    @After
    public void finalizaPage(){
        this.driver.close();
    }

    public void setEmail(String email){
        CadastroBarriga.email = email;
    }

    public String getEmail(){
        return email;
    }
}
