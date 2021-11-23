
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.02.09
; Description:
; Version: v0.5.8
; Compatibility: x4.2.4



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.server-views.admin
    (:require [mid-fruits.candy   :refer [param]]
              [mid-fruits.keyword :as keyword]
              [x.server-ui.api    :as ui]
              [x.server-user.api  :as user]))



;; -- Components --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- label
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) request
  ;
  ; @return (hiccup)
  [_]
  [:div.x-label {:data-color            (keyword/to-dom-value :muted)
                 :data-horizontal-align (keyword/to-dom-value :center)
                 :data-layout           (keyword/to-dom-value :row)}
                [:div.x-label--body "How would you like to log in?"]])

(defn- login-via-app-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) request
  ;
  ; @return (hiccup)
  [_]
  [:div.x-button {:data-color            (keyword/to-dom-value :secondary)
                  :data-horizontal-align (keyword/to-dom-value :center)
                  :data-layout           (keyword/to-dom-value :row)
                  :data-variant          (keyword/to-dom-value :outlined)}
                 [:button.x-button--body
                   {:onClick "window.location.href='/login'"}
                   [:div.x-button--label "Via app"]]])

(defn- registration-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) request
  ;
  ; @return (hiccup)
  [_]
  [:div.x-button {:data-horizontal-align (keyword/to-dom-value :center)
                  :data-layout           (keyword/to-dom-value :row)
                  :data-variant          (keyword/to-dom-value :transparent)}
                 [:a.x-button--body
                   {:href "/create-account"}
                   [:div.x-button--label "Create an account"]]])

(defn- app-login-selector
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) request
  ;
  ; @return (hiccup)
  [request]
  [:div#x-app-login-selector
    (label                request)
    (login-via-app-button request)
    (registration-button  request)])

(defn- admin
  ; @param (map) request
  ; @param (map)(opt) body-props
  ;  {:shield (hiccup)(opt)}
  ;
  ; @return (hiccup)
  [request {:keys [shield]}]
  [:body#x-body-container
    {:data-theme (user/request->user-settings-item request :selected-theme)
     :data-nosnippet "true"}
    (if (some? shield)
        (param shield))])

(defn view
  ; @param (map) request
  ; @param (map)(opt) body-props
  ;
  ; @usage
  ;  (views/app-admin {...})
  ;
  ; @return (hiccup)
  ([request]
   (view request {}))

  ([request _]
   (let [shield     (ui/app-shield (app-login-selector request))
         body-props (ui/body-props-prototype request {:shield shield})]
        (admin request body-props))))
