import Image from "next/image";
import Navbar from "../components/Navbar";
import {useState, useEffect} from "react";
import {GoogleLogin} from "react-google-login";
import background from "../public/login.jpeg";
import Cookies from "js-cookie";
import FacebookLogin from "react-facebook-login";
import {useRouter} from "next/router";
import Spinner from "../components/Spinner";
import Link from "next/link";
import Fade from 'react-reveal/Fade';
import Slide from 'react-reveal/Fade';
import {Divider} from 'antd';

export default function Login() {
    const router = useRouter();
    const axios = require("axios");
    const [usernameResponse, setUsername] = useState("");
    const [passwordResponse, setPassword] = useState("");
    const [errorMessage, setErrorMessage] = useState("");
    const [loading, setLoading] = useState(false);
    const responseGoogle = (response) => {
        console.log(response)
        console.log(response.$b.access_token);
        fetchMyAPI(response.$b.access_token, "Google");
    };
    const responseFacebook = (response) => {
        console.log(response.$b.access_token);
        fetchMyAPI(response.$b.access_token,"Facebook");
    };
    async function submitLoginCredentials() {
        setLoading(true)
        await axios
            .post("https://locus-g3gtexqeba-uc.a.run.app/authenticate", {
                username: usernameResponse,
                password: passwordResponse,
            })
            .then(function (response1) {
                setErrorMessage("Successfully login");
                Cookies.set("token", response1.data.token);
                Cookies.set("username", response1.data.name);
                Cookies.set("id", response1.data.id)
                router.push("/homeloggedin");
            })
            .catch(function (error) {
                setErrorMessage("Invalid username/password");
                console.log(error);
            });
    }

    async function fetchMyAPI(response, type) {
        setLoading(true)
        if (type == "Google") {
            await axios
                .post("https://locus-g3gtexqeba-uc.a.run.app/google/signin?token=" + response, {})
                .then(function (response1) {
                    console.log(response1);
                    console.log(response1.data.id)
                    Cookies.set("id", response1.data.id)
                    Cookies.set("token", response1.data.token);
                    Cookies.set("username", response1.data.name);
                    router.push("/homeloggedin");
                })
                .catch(function (error) {
                    console.log(error);
                });
            }else{
                axios.post('/facebook/signin?token=' + response , {
                })
                .then(function (response1) {
                  console.log("hello"+response1);
                  Cookies.set('token',response1.data.token)
                })
                .catch(function (error) {
                  console.log(error);
                });
        }
    }

    useEffect(() => {
        console.log(Cookies.get("token"));
    }, []);

    return (
        <> {loading ? <Spinner/> :
            <div className="h-screen w-screen flex-col flex items-center justify-center">
                <div
                    style={{
                        backgroundImage: "url(/login.jpeg)",
                        backgroundPosition: "center",
                        backgroundSize: "cover",
                        backgroundRepeat: "no-repeat",
                        width: "100vw",
                        height: "100vh",
                        opacity: 0.2,
                        position: "absolute",
                        zIndex: -1,
                    }}
                />
                <Fade bottom>
                    <div className="absolute top-0 left-2">
                        <img alt=" " src="/logo.png" height={100} width={150} className="ml-3"/>
                    </div>
                    <div className="flex-col flex border p-5 bg-white rounded-xl shadow-xl">
                <span style={{fontSize: 35}} className="mb-1 font-bold">
                    Sign in
                </span>
                        <span className="mb-4" style={{fontSize: 14}}>
                    Start joining and hosting events with Locus
                </span>
                        {errorMessage !== "" ? (
                            <span
                                className="mb-4 w-full text-center text-red-500"
                                style={{fontSize: 14}}
                            >
                        {errorMessage}
                    </span>
                        ) : (
                            <div/>
                        )}

                        <input
                            placeholder="Username/Email Address"
                            className="rounded border mb-4 h-12 px-3 w-96 rounded"
                            style={{fontSize: 13}}
                            onChange={(e) => setUsername(e.target.value)}
                        />
                        <input
                            type="password"
                            placeholder={"Password"}
                            className="rounded border mb-4 h-12 px-3"
                            onChange={(e) => {
                                setPassword(e.target.value);
                            }}
                        />
                        <button
                            onClick={() => {
                                console.log("Hello");
                            }}
                            className="self-start mb-8 rounded"
                            style={{color: "#32BEA6"}}
                        >
                            Forgot password?
                        </button>
                        <button
                            className="w-full items-center py-2 px-4 rounded-full"
                            style={{backgroundColor: "#32BEA6", color: "white"}}
                            onClick={() => submitLoginCredentials()}
                        >
                            <span style={{fontSize: 16}}>Sign In</span>
                        </button>
                        <Divider plain style={{alignItems: "start"}}>OR</Divider>
                        <div className="text-center mt-2">
                            <GoogleLogin
                                theme="dark"
                                clientId="510265715964-60hka08qs988tarj2bcgk8o7olkbuhnf.apps.googleusercontent.com"
                                buttonText="Sign-in with Google"
                                className="text-center w-3/4 content-center"
                                onSuccess={responseGoogle}
                                onFailure={responseGoogle}
                            />
                            <FacebookLogin
                    appId="3139977946220316"
                    textButton="Login"
                    onSuccess={responseFacebook}
                    onFailure ={()=>console.log("Failed")}
                />
                        </div>

                    </div>
                    <Slide>
                        <div className="mt-5">
                    <span>
                        New to Locus?</span><a style={{color: "#2b9e8b"}} className="font-bold" href="/signup">Join
                            Now</a>
                        </div>
                    </Slide>
                </Fade>
            </div>}
        </>
    );
}
