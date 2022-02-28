
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-tools.image-loader.engine
    (:require [app-fruits.reagent :refer [ratom]]
              [mid-fruits.candy   :refer [param]]
              [mid-fruits.css     :as css]
              [x.app-core.api     :as a]))



;; -- State -------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @atom (map)
(defonce LOADERS (ratom {}))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn image-loaded
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) loader-id
  [loader-id]
  (swap! LOADERS assoc-in [loader-id :loaded?] true))

(defn image-loaded?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) loader-id
  ;
  ; @return (boolean)
  [loader-id]
  (boolean (get-in @LOADERS [loader-id :loaded?])))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn container-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) loader-id
  ; @param (map) loader-props
  ;  {:class (keyword or keywords in vector)(opt)
  ;   :uri (string)}
  ;
  ; @return (map)
  ;  {:class (keyword or keywords in vector)
  ;   :id (keyword)
  ;   :style (map)}
  [loader-id {:keys [class uri]}]
  {:class (param class)
   :id    (a/dom-value loader-id)
   :style (if-let [image-loaded? (image-loaded? loader-id)]
                  {:backgroundImage (css/url uri)})})
