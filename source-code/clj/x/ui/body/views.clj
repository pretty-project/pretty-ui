
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.ui.body.views
    (:require [candy.api                    :refer [param]]
              [ring.middleware.anti-forgery :refer [*anti-forgery-token*]]
              [x.ui.body.helpers            :as body.helpers]
              [x.ui.body.prototypes         :as body.prototypes]
              [x.ui.core.helpers            :refer [include-js]]
              [x.ui.loading-screen.views    :as loading-screen.views]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- body
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) request
  ; @param (map) body-props
  ;  {:loading-screen (hiccup)(opt)
  ;   :selected-theme (string)}
  [request {:keys [loading-screen selected-theme]}]
  [:body#x-body-container {:data-hide-scrollbar true
                           :data-theme          selected-theme}
                          (let [csrf-token (force *anti-forgery-token*)]
                               [:div#sente-csrf-token {:data-csrf-token csrf-token}])
                          [:div#x-app-container]
                          (loading-screen.views/view request loading-screen)])

(defn view
  ; @param (map) request
  ; @param (map)(opt) body-props
  ;  {:loading-screen (hiccup)(opt)
  ;   :plugin-js-paths (maps in vector)
  ;    [{:js-build (keyword)(opt)
  ;      :route-template (string)(opt)
  ;      :uri (string)}]}
  ;
  ; @usage
  ;  (body {...} {...})
  ([request]
   (view request {}))

  ([request body-props]
   (let [body-props (body.prototypes/body-props-prototype request body-props)]
        (-> (body                           request body-props)
            (body.helpers/body<-js-includes request body-props)))))
