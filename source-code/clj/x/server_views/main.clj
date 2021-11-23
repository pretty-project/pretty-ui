
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2020.10.03
; Description:
; Version: v1.2.8
; Compatibility: x3.9.9



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.server-views.main
    (:require [mid-fruits.candy  :refer [param return]]
              [x.server-ui.api   :as ui]
              [x.server-user.api :as user]
              ; DEBUG
              [playground.api    :as playground]))



;; -- Components --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- main
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) request
  ; @param (map) body-props
  ;  {:shield (hiccup)(opt)}
  ;
  ; @return (hiccup)
  [request {:keys [shield]}]
  [:body#x-body-container
    {:data-theme (user/request->user-settings-item request :selected-theme)}
    [:div#x-app-container]
    (if (some? shield)
        (param shield))])

(defn view
  ; @param (map) request
  ; @param (map)(opt) body-props
  ;  {:app-build (string)(opt)
  ;   :plugin-js-paths (maps in vector)
  ;    [{:cache-control? (boolean)(opt)
  ;       Default: false
  ;      :uri (string)}]
  ;   :shield (hiccup)(opt)}
  ;
  ; @usage
  ;  (views/app-main {...} {:shield [:div#x-app-shield "My loading screen"]})
  ;
  ; @return (hiccup)
  ([request]
   (view request {}))

  ([request body-props]
   (let [body-props (ui/body-props-prototype request body-props)
         _          (playground/debug!       request)]
        (-> (main request body-props)
            (ui/body<-js-includes request body-props)))))
