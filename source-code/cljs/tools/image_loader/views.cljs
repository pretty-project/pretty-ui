
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript web application framework
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns tools.image-loader.views
    (:require [candy.api                  :refer [param]]
              [random.api                 :as random]
              [tools.image-loader.helpers :as helpers]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- loading-icon
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) loader-id
  ; @param (map) loader-props
  [loader-id _]
  (if-not (image-loader.helpers/image-loaded? loader-id)
          [:div.x-column {:data-vertical-align :center}
                         [:i.x-icon            :image]]))

(defn- control-image
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) loader-id
  ; @param (map) loader-props
  ; {:uri (string)}
  [loader-id {:keys [uri]}]
  [:img {:on-load #(image-loader.helpers/image-loaded loader-id)
         :src      (param uri)
         :style    {:display "none"}}])

(defn- image-loader
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) loader-id
  ; @param (map) loader-props
  [loader-id loader-props]
  [:div (helpers/container-attributes loader-id loader-props)
        [control-image                loader-id loader-props]
        [loading-icon                 loader-id loader-props]])

(defn component
  ; @param (keyword)(opt) loader-id
  ; @param (map) loader-props
  ; {:class (keyword or keywords in vector)(opt)
  ;  :uri (string)}
  ;
  ; @usage
  ; [component {...}]
  ;
  ; @usage
  ; [component :my-loader {...}]
  ([loader-props]
   [component (random/generate-keyword) loader-props])

  ([loader-id loader-props]
   [image-loader loader-id @loader-props]))
