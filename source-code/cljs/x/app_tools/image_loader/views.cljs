
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-tools.image-loader.views
    (:require [mid-fruits.candy                :refer [param]]
              [x.app-core.api                  :as a]
              [x.app-tools.image-loader.engine :as image-loader.engine]))



;; -- Components --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- loading-icon
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) loader-id
  ; @param (map) loader-props
  [loader-id _]
  (if-not (image-loader.engine/image-loaded? loader-id)
          [:div.x-column {:data-vertical-align :center}
                         [:i.x-icon            :image]]))

(defn- control-image
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) loader-id
  ; @param (map) loader-props
  ;  {:uri (string)}
  [loader-id {:keys [uri]}]
  [:img {:on-load #(image-loader.engine/image-loaded loader-id)
         :src      (param uri)
         :style    {:display "none"}}])

(defn- image-loader
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) loader-id
  ; @param (map) loader-props
  [loader-id loader-props]
  [:div (image-loader.engine/container-attributes loader-id loader-props)
        [control-image                            loader-id loader-props]
        [loading-icon                             loader-id loader-props]])

(defn component
  ; @param (keyword)(opt) loader-id
  ; @param (map) loader-props
  ;  {:class (keyword or keywords in vector)(opt)
  ;   :uri (string)}
  ;
  ; @usage
  ;  [tools/image-loader {...}]
  ;
  ; @usage
  ;  [tools/image-loader :my-loader {...}]
  ([loader-props]
   [component (a/id) loader-props])

  ([loader-id loader-props]
   [image-loader loader-id @loader-props]))
