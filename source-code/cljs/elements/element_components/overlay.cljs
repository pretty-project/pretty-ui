
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns elements.element-components.overlay
    (:require [elements.engine.api  :as engine]
              [mid-fruits.candy     :refer [param]]
              [mid-fruits.random    :as random]
              [x.app-components.api :as x.components]))



;; -- Prototypes --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- overlay-props-prototype
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) overlay-props
  ;
  ; @return (map)
  ;  {}
  [overlay-props]
  (merge {}
         (param overlay-props)))



;; -- Components --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- overlay
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) overlay-id
  ; @param (map) overlay-props
  ;  {:content (metamorphic-content)}
  [overlay-id {:keys [content] :as overlay-props}]
  [:div.e-overlay (engine/element-attributes overlay-id overlay-props)
                  [x.components/content overlay-id content]])

(defn element
  ; @param (keyword)(opt) overlay-id
  ; @param (map) overlay-props
  ;  {:class (keyword or keywords in vector)(opt)
  ;   :content (metamorphic-content)
  ;   :disabled? (boolean)(opt)
  ;    Default: false
  ;   :style (map)(opt)}
  ;
  ; @usage
  ;  [overlay {...}]
  ;
  ; @usage
  ;  [overlay :my-overlay {...}]
  ([overlay-props]
   [element (random/generate-keyword) overlay-props])

  ([overlay-id overlay-props]
   (let [];overlay-props (overlay-props-prototype overlay-props)
        [overlay overlay-id overlay-props])))
