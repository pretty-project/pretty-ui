
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.02.14
; Description:
; Version: v0.4.0
; Compatibility: x4.3.9



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-elements.content-bar
    (:require [mid-fruits.candy          :refer [param]]
              [x.app-components.api      :as components]
              [x.app-core.api            :as a]
              [x.app-elements.engine.api :as engine]))



;; -- Prototypes --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- bar-props-prototype
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) bar-props
  ;
  ; @return (map)
  ;  {:color (keyword)
  ;   :container-stretch-orientation (keyword)}
  [bar-props]
  (merge {:color :primary}
         (param bar-props)
         {:container-stretch-orientation :horizontal}))



;; -- Components --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- content-bar-body
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) bar-id
  ; @param (map) bar-props
  ;
  ; @return (hiccup)
  [bar-id bar-props]
  (let [content-props (components/extended-props->content-props bar-props)]
       [:div.x-content-bar--body [components/content bar-id content-props]]))

(defn- content-bar
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) bar-id
  ; @param (map) bar-props
  ;
  ; @return (hiccup)
  [bar-id bar-props]
  [:div.x-content-bar
    (engine/element-attributes bar-id bar-props)
    [content-bar-body          bar-id bar-props]])

(defn view
  ; XXX#8711
  ; A content-bar elem az x.app-components.api/content komponens használatával jeleníti meg
  ; a számára :content tulajdonságként átadott tartalmat.
  ; A content-bar elemnél alkalmazott :content, :content-props és :subscriber tulajdonságok
  ; használatának leírását az x.app-components.api/content komponens dokumentációjában találod.
  ;
  ; @param (keyword)(opt) bar-id
  ; @param (map) bar-props
  ;  {:color (keyword)(opt)
  ;    :primary, :secondary, :muted
  ;    Default: :primary
  ;   :class (string or vector)(opt)
  ;   :content (metamorphic-content)
  ;   :content-props (map)(opt)
  ;   :layout (keyword)(opt)
  ;    :footer, :header
  ;   :style (map)(opt)
  ;   :subscriber (subscription vector)(opt)}
  ;
  ; @usage
  ;  XXX#7610
  ;  A content-bar elemen megjelenő tartalom használatának leírását a blank elem dokumentációjában találod.
  ;
  ; @usage
  ;  [elements/content-bar {...}]
  ;
  ; @usage
  ;  [elements/content-bar :my-content-bar {...}]
  ;
  ; @return (component)
  ([bar-props]
   [view nil bar-props])

  ([bar-id bar-props]
   (let [bar-id    (a/id bar-id)
         bar-props (a/prot bar-props bar-props-prototype)]
        [engine/container bar-id
          {:base-props bar-props
           :component  content-bar}])))
