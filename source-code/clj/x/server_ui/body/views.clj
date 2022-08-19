
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.server-ui.body.views
    (:require [mid-fruits.candy             :refer [param]]
              [ring.middleware.anti-forgery :refer [*anti-forgery-token*]]
              [x.server-ui.body.helpers     :as body.helpers]
              [x.server-ui.body.prototypes  :as body.prototypes]
              [x.server-ui.core.helpers     :refer [include-js]]))



;; -- Components --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- body
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) request
  ; @param (map) body-props
  ;  {:selected-theme (string)
  ;   :shield (hiccup)(opt)}
  [request {:keys [selected-theme shield]}]
  [:body#x-body-container {:data-theme selected-theme}
                          (let [csrf-token (force *anti-forgery-token*)]
                               [:div#sente-csrf-token {:data-csrf-token csrf-token}])
                          [:div#x-app-container]
                          (param shield)])

(defn view
  ; @param (map) request
  ; @param (map)(opt) body-props
  ;  {:plugin-js-paths (maps in vector)
  ;    [{:core-js (string)(opt)
  ;      :uri (string)}]
  ;   :shield (hiccup)(opt)}
  ;
  ; @usage
  ;  (ui/body {...} {:shield [:div#x-app-shield "My loading screen"]})
  ([request]
   (view request {}))

  ([request body-props]
   (let [body-props (body.prototypes/body-props-prototype request body-props)]
        (-> (body                           request body-props)
            (body.helpers/body<-js-includes request body-props)))))
