
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns elements.element-components.horizontal-line
    (:require [mid-fruits.candy    :refer [param]]
              [css.api             :as css]
              [elements.engine.api :as engine]
              [mid-fruits.random   :as random]))



;; -- Helpers -----------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- line-body-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) line-id
  ; @param (map) line-props
  ;  {:strength (px)}
  ;
  ; @return (map)
  ;  {:style (map)
  ;    {:height (string)}}
  [_ {:keys [strength] :as line-props}]
  (cond-> {:style {:height (css/px strength)}}))



;; -- Prototypes --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- line-props-prototype
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) line-props
  ;
  ; @return (map)
  ;  {:color (keyword or string)
  ;   :strength (px)}
  [line-props]
  (merge {:color    :muted
          :strength 1}
         (param line-props)))



;; -- Components --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- horizontal-line
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) line-id
  ; @param (map) line-props
  [line-id line-props]
  [:div.e-horizontal-line (engine/element-attributes line-id line-props)
                          [:div.e-horizontal-line--body (line-body-attributes line-id line-props)]])

(defn element
  ; @param (keyword)(opt) line-id
  ; @param (map) line-props
  ;  {:color (keyword or string)(opt)
  ;    :highlight, :muted, :primary, :secondary
  ;    Default: :muted
  ;   :strength (px)(opt)
  ;    Default: 1}
  ;
  ; @usage
  ;  [horizontal-line {...}]
  ;
  ; @usage
  ;  [horizontal-line :my-horizontal-line {...}]
  ([line-props]
   [element (random/generate-keyword) line-props])

  ([line-id line-props]
   (let [line-props (line-props-prototype line-props)]
        [horizontal-line line-id line-props])))