
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.10.27
; Description:
; Version: v0.2.8
; Compatibility: x4.4.8



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-elements.overlay
    (:require [mid-fruits.candy          :refer [param]]
              [x.app-components.api      :as components]
              [x.app-core.api            :as a]
              [x.app-elements.engine.api :as engine]))



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
  ;
  ; @return (hiccup)
  [overlay-id overlay-props]
  (let [content-props (components/extended-props->content-props overlay-props)]
       [:div.x-overlay (engine/element-attributes overlay-id overlay-props)
                       [components/content overlay-id content-props]]))

(defn element
  ; XXX#8711
  ; Az overlay elem az x.app-components.api/content komponens használatával jeleníti meg
  ; a számára :content tulajdonságként átadott tartalmat.
  ; Az overlay elemnél alkalmazott :content, :content-props és :subscriber tulajdonságok
  ; használatának leírását az x.app-components.api/content komponens dokumentációjában találod.
  ;
  ; @param (keyword)(opt) overlay-id
  ; @param (map) overlay-props
  ;  {:class (keyword or keywords in vector)(opt)
  ;   :content (metamorphic-content)
  ;   :content-props (map)(opt)
  ;   :disabled? (boolean)(opt)
  ;    Default: false
  ;   :style (map)(opt)
  ;   :subscriber (subscription-vector)(opt)}
  ;
  ; @usage
  ;  XXX#7610
  ;  Az overlay elemen megjelenő tartalom használatának leírását a blank elem dokumentációjában találod.
  ;
  ; @usage
  ;  [elements/overlay {...}]
  ;
  ; @usage
  ;  [elements/overlay :my-overlay {...}]
  ;
  ; @return (component)
  ([overlay-props]
   [element (a/id) overlay-props])

  ([overlay-id overlay-props]
   (let [];overlay-props (overlay-props-prototype overlay-props)
        [overlay overlay-id overlay-props])))