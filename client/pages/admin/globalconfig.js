import {useEffect, useState} from "react";
import AdminNavbar from "../../components/AdminNavbar";
import Spinner from "../../components/Spinner";
import {Breadcrumb, Col, InputNumber, notification, PageHeader, Slider} from 'antd';
import {HomeOutlined} from '@ant-design/icons';
import 'toastr/build/toastr.css';
import Cookies from 'js-cookie';
import {useRouter} from "next/router";


export default function GlobalConfig() {
    const router = useRouter();

    // Show Spin while loading is true
    const [userName, setUsername] = useState(true);
    const [loading, setLoading] = useState(true);

    // Event capacity
    const [AttractionsValue, setAttractionsValue] = useState(0);
    const [CRCValue, setCRCValue] = useState(0);
    const [FuneralValue, setFuneralValue] = useState(0);
    const [MarriageValue, setMarriageValue] = useState(0);
    const [MICEValue, setMICEValue] = useState(0);
    const [HotelsValue, setHotelsValue] = useState(0);
    const [SportsValue, setSportsValue] = useState(0);
    const [ReligiousValue, setReligiousValue] = useState(0);
    const [OthersValue, setOthersValue] = useState(0);

    // Allocation for Data
    const [data, setData] = useState([]);
    const [allData, setAllData] = useState([]);

    // Fetch Database Data onLoad
    const axios = require("axios");
    const style = {
        display: 'inline-block',
        height: 300,
        marginLeft: 30,
        marginRight: 30,
    };


    useEffect(async () => {
        axios.post("https://locus-g3gtexqeba-uc.a.run.app/validate", {}, {withCredentials: true})
            .then(function (response) {
                console.log(response)
            }).catch(function (error) {
            router.push("/login");
            console.log(error)
        })

        axios.get("https://locus-g3gtexqeba-uc.a.run.app/event/type/list", {withCredentials: true})
            .then(function (response) {
                setCRCValue(response.data[0].capacity)
                setFuneralValue(response.data[1].capacity)
                setMarriageValue(response.data[2].capacity)
                setMICEValue(response.data[3].capacity)
                setHotelsValue(response.data[4].capacity)
                setSportsValue(response.data[5].capacity)
                setReligiousValue(response.data[6].capacity)
                setOthersValue(response.data[7].capacity)
                setAttractionsValue(response.data[8].capacity)
                console.log(response.data)
            }).catch(function (error) {
            console.log(error)
        })

        // 0: {id: 1, type: 'Country and recreation clubs', capacity: 50}
        // 1: {id: 2, type: 'Funeral events', capacity: 30}
        // 2: {id: 3, type: 'Marriage solemnisations and wedding receptions', capacity: 1000}
        // 3: {id: 4, type: 'MICE events', capacity: 1000}
        // 4: {id: 5, type: 'Hotels', capacity: 1000}
        // 5: {id: 6, type: 'Sports sector enterprises, sports education, and premises with sports facilities', capacity: 30}
        // 6: {id: 7, type: 'Religious organisations', capacity: 1000}
        // 7: {id: 8, type: 'Others', capacity: 1000}
        // 8: {id: 9, type: 'Attractions', capacity: 1000}


        document.title = 'Locus | Admin Global Settings';
        if (Cookies.get('username') !== undefined) {
            setUsername(Cookies.get('username'))
        }

        setLoading(false)
    }, []);

    const updateSuccessNotification = (type) => {
        notification[type]({
            message: "Success",
            description:
                "Participants limit has been updated!",
        });
    };

    const updateCapacity = (e) => {
        setLoading(true);
        axios.put("https://locus-g3gtexqeba-uc.a.run.app/event/type/1", {
            id: 1,
            type: "Country and recreation clubs",
            capacity: CRCValue,
        }, {withCredentials: true}).then(function (response) {
                axios.put("https://locus-g3gtexqeba-uc.a.run.app/event/type/2", {
                    id: 2,
                    type: "Funeral events",
                    capacity: FuneralValue,
                }, {withCredentials: true}).then(function (response) {
                        axios.put("https://locus-g3gtexqeba-uc.a.run.app/event/type/3", {
                            id: 3,
                            type: "Marriage solemnisations and wedding receptions",
                            capacity: MarriageValue,
                        }, {withCredentials: true}).then(function (response) {
                                axios.put("https://locus-g3gtexqeba-uc.a.run.app/event/type/4", {
                                    id: 4,
                                    type: "MICE events",
                                    capacity: MICEValue,
                                }, {withCredentials: true}).then(function (response) {
                                        axios.put("https://locus-g3gtexqeba-uc.a.run.app/event/type/5", {
                                            id: 5,
                                            type: "Hotels",
                                            capacity: HotelsValue,
                                        }, {withCredentials: true}).then(function (response) {
                                                axios.put("https://locus-g3gtexqeba-uc.a.run.app/event/type/6", {
                                                    id: 6,
                                                    type: "Sports sector enterprises, sports education, and premises with sports facilities",
                                                    capacity: SportsValue,
                                                }, {withCredentials: true}).then(function (response) {
                                                        axios.put("https://locus-g3gtexqeba-uc.a.run.app/event/type/7", {
                                                            id: 7,
                                                            type: "Religious organisations",
                                                            capacity: ReligiousValue,
                                                        }, {withCredentials: true}).then(function (response) {
                                                                axios.put("https://locus-g3gtexqeba-uc.a.run.app/event/type/8", {
                                                                    id: 8,
                                                                    type: "Others",
                                                                    capacity: OthersValue,
                                                                }, {withCredentials: true}).then(function (response) {
                                                                        axios.put("https://locus-g3gtexqeba-uc.a.run.app/event/type/9", {
                                                                            id: 9,
                                                                            type: "Attractions",
                                                                            capacity: AttractionsValue,
                                                                        }, {withCredentials: true}).then(function (response) {
                                                                                setLoading(false)
                                                                                updateSuccessNotification("success")
                                                                            }
                                                                        ).catch(function (error) {
                                                                            console.log(error)
                                                                        })
                                                                    }
                                                                ).catch(function (error) {
                                                                    console.log(error)
                                                                })
                                                            }
                                                        ).catch(function (error) {
                                                            console.log(error)
                                                        })
                                                    }
                                                ).catch(function (error) {
                                                    console.log(error)
                                                })
                                            }
                                        ).catch(function (error) {
                                            console.log(error)
                                        })
                                    }
                                ).catch(function (error) {
                                    console.log(error)
                                })
                            }
                        ).catch(function (error) {
                            console.log(error)
                        })
                    }
                ).catch(function (error) {
                    console.log(error)
                })
            }
        ).catch(function (error) {
            console.log(error)
        })
    }


    return (
        <>
            {loading ? <Spinner/> :
                <>
                    <AdminNavbar user={Cookies.get('username')} userID={Cookies.get('id')}/>
                    <div className="px-16">
                        <Col flex="100px"></Col>
                        <Col flex="auto">
                            <Breadcrumb style={{paddingTop: 20}}>
                                <Breadcrumb.Item href="/admin">
                                    <HomeOutlined style={{display: 'inline-flex'}}/>
                                </Breadcrumb.Item>
                                <Breadcrumb.Item>
                                    <span>Global Settings</span>
                                </Breadcrumb.Item>
                            </Breadcrumb>
                            <PageHeader
                                className="site-page-header"
                                title="Verification"
                                style={{paddingTop: 0, paddingBottom: 0, paddingLeft: 0}}
                            />
                            <p style={{paddingBottom: 30}}>Modify Participant Maxed Capacity by Event Type</p>
                            <div className="grid grid-cols-5 gap-4 pb-4 pr-4">
                                <div className="col-start-1 col-end-2">
                                    <div className="p-8">
                                        <header className="font-semibold mb-3">Attractions</header>
                                        <Slider
                                            min={1}
                                            max={1000}
                                            onChange={value => setAttractionsValue(value)}
                                            value={typeof AttractionsValue === 'number' ? AttractionsValue : 0}
                                        />
                                        <InputNumber
                                            min={1}
                                            max={1000}
                                            value={AttractionsValue}
                                            onChange={value => setAttractionsValue(value)}
                                        />
                                    </div>
                                </div>

                                <div className="col-start-2 col-end-3">
                                    <div className="p-8">
                                        <header className="font-semibold mb-3">Recreation Clubs</header>
                                        <Slider
                                            min={1}
                                            max={1000}
                                            onChange={value => setCRCValue(value)}
                                            value={typeof setCRCValue === 'number' ? setCRCValue : 0}
                                        />
                                        <InputNumber
                                            min={1}
                                            max={1000}
                                            value={CRCValue}
                                            onChange={value => setCRCValue(value)}
                                        />
                                    </div>
                                </div>

                                <div className="col-start-3 col-end-4">
                                    <div className="p-8">
                                        <header className="font-semibold mb-3">Funeral</header>
                                        <Slider
                                            min={1}
                                            max={1000}
                                            onChange={value => setFuneralValue(value)}
                                            value={typeof FuneralValue === 'number' ? FuneralValue : 0}
                                        />
                                        <InputNumber
                                            min={1}
                                            max={1000}
                                            value={FuneralValue}
                                            onChange={value => setFuneralValue(value)}
                                        />
                                    </div>
                                </div>

                                <div className="col-start-4 col-end-5">
                                    <div className="p-8">
                                        <header className="font-semibold mb-3">Marriage/Solemnisation
                                        </header>
                                        <Slider
                                            min={1}
                                            max={1000}
                                            onChange={value => setMarriageValue(value)}
                                            value={typeof MarriageValue === 'number' ? MarriageValue : 0}
                                        />
                                        <InputNumber
                                            min={1}
                                            max={1000}
                                            value={MarriageValue}
                                            onChange={value => setMarriageValue(value)}
                                        />
                                    </div>
                                </div>
                                <div className="col-start-5 col-end-6">
                                    <div className="p-8">
                                        <header className="font-semibold mb-3">MICE Event</header>
                                        <Slider
                                            min={1}
                                            max={1000}
                                            onChange={value => setMICEValue(value)}
                                            value={typeof MICEValue === 'number' ? MICEValue : 0}
                                        />
                                        <InputNumber
                                            min={1}
                                            max={1000}
                                            value={MICEValue}
                                            onChange={value => setMICEValue(value)}
                                        />
                                    </div>
                                </div>
                            </div>
                            <div className="grid grid-cols-5 gap-4 pb-4 pr-4">
                                <div className="col-start-1 col-end-2">
                                    <div className="p-8">
                                        <header className="font-semibold mb-3">Hotels</header>
                                        <Slider
                                            min={1}
                                            max={1000}
                                            onChange={value => setHotelsValue(value)}
                                            value={typeof HotelsValue === 'number' ? HotelsValue : 0}
                                        />
                                        <InputNumber
                                            min={1}
                                            max={1000}
                                            value={HotelsValue}
                                            onChange={value => setHotelsValue(value)}
                                        />
                                    </div>
                                </div>
                                <div className="col-start-2 col-end-3">
                                    <div className="p-8">
                                        <header className="font-semibold mb-3">Sports Events</header>
                                        <Slider
                                            min={1}
                                            max={1000}
                                            onChange={value => setSportsValue(value)}
                                            value={typeof SportsValue === 'number' ? SportsValue : 0}
                                        />
                                        <InputNumber
                                            min={1}
                                            max={1000}
                                            value={SportsValue}
                                            onChange={value => setSportsValue(value)}
                                        />
                                    </div>
                                </div>
                                <div className="col-start-3 col-end-4">
                                    <div className="p-8">
                                        <header className="font-semibold mb-3">Religious Event</header>
                                        <Slider
                                            min={1}
                                            max={1000}
                                            onChange={value => setReligiousValue(value)}
                                            value={typeof ReligiousValue === 'number' ? ReligiousValue : 0}
                                        />
                                        <InputNumber
                                            min={1}
                                            max={1000}
                                            value={ReligiousValue}
                                            onChange={value => setReligiousValue(value)}
                                        />
                                    </div>
                                </div>
                                <div className="col-start-4 col-end-5">
                                    <div className="p-8">
                                        <header className="font-semibold mb-3">Others</header>
                                        <Slider
                                            min={1}
                                            max={1000}
                                            onChange={value => setOthersValue(value)}
                                            value={typeof OthersValue === 'number' ? OthersValue : 0}
                                        />
                                        <InputNumber className="content-center justify-center flex -align-center"
                                                     min={1}
                                                     max={1000}
                                                     value={OthersValue}
                                                     onChange={value => setOthersValue(value)}
                                        />
                                    </div>
                                </div>
                                <div className="col-start-3 col-end-4 justify-center flex -align-center">
                                    <button onClick={updateCapacity}
                                            className="mb-20 bg-green-500 hover:bg-green-400 text-white font-bold py-2 px-4 rounded-full">
                                        Update Capacity Guideline
                                    </button>
                                </div>

                            </div>
                        </Col>
                    </div>
                </>
            }
        </>
    );
}
