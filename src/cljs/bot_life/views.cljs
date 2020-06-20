(ns bot-life.views
  (:require
   [reagent.core :as reagent]
   [re-frame.core :as re-frame]
   [bot-life.subs :as subs]
   ))


;; home

(defn home-panel []
  (let [name (re-frame/subscribe [::subs/name])]
    [:div
     [:h1 (str "Hello from " @name ". This is the Home Page.")]

     [:div
      [:a {:href "#/about"} "go to About Page"]
      [:a {:href "#/login"} "go to Login Page"]]
     ]))


;; about

(defn about-panel []
  [:div
   [:h1 "This is the About Page."]

   [:div
    [:a {:href "#/"} "go to Home Page"]
    [:a {:href "#/login"} "go to Login Page"]
    ]])


;; login

(defn login-panel []
  [:div
    [:h1 "Login"]

    [:form
      [:label "login:" [:input {:type "text"}]]
      [:label "password:" [:input {:type "password"}]]
      ]])


;; side
(defn- link-style [active?]
  (merge
    {}
    (if active? {} {})))

(defn menu-item [text href is-active]
  (let [style (merge {}
                     (if is-active {} {}))]
    [:li [:a {:href href} text]]))

(defn user-block [{:keys [is-authorized username]}]
  (let [*form-state (reagent/atom (if is-authorized :authorized :hidden))]
    (case @*form-state
      :hidden [:div "Welcome!"]
      :sign-in [:div
                  "sign in form"
                  [:button "sign in"]]
      :authorized [:div "Hello, " username]
      [:div "default"])))

(defn side-menu-panel []
  (let [*active-panel (re-frame/subscribe [::subs/active-panel])
        *current-user (re-frame/subscribe [::subs/current-user])]
    [:div 
      [:ul
        (user-block *current-user)
        (menu-item "home" "#/" (= *active-panel :home-panel))
        (menu-item "login" "#/login" (= *active-panel :login-panel))
        (menu-item "about" "#/about" (= *active-panel :about-panel))
      ]
    ]))

;; main

(defn- panels [panel-name]
  (case panel-name
    :home-panel [home-panel]
    :about-panel [about-panel]
    :login-panel [login-panel]
    [:div]))

(defn show-panel [panel-name]
  [panels panel-name])

(defn main-panel []
  (let [*active-panel (re-frame/subscribe [::subs/active-panel])]
    [:div {:style {:display "grid"
                   :grid-template-columns "240px auto"
                   :grid-template-rows "auto"
                   :grid-template-areas "\"side main\""}}
      [:div {:style {:grid-area "side"}} (side-menu-panel)]
      [:div {:style {:grid-area "main"}} (show-panel @*active-panel)]
      
      
    ]
    ))
