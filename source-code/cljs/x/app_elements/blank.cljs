
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.02.22
; Description:
; Version: v0.1.8
; Compatibility: x4.4.8



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-elements.blank
    (:require [mid-fruits.candy          :refer [param]]
              [x.app-components.api      :as components]
              [x.app-core.api            :as a :refer [r]]
              [x.app-elements.engine.api :as engine]))



;; -- Prototypes --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- blank-props-prototype
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) blank-props
  ;
  ; @return (map)
  [blank-props]
  (merge {}
         (param blank-props)))



;; -- Components --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- blank
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) blank-id
  ; @param (map) blank-props
  ;  {:content (metamorphic-content)}
  ;
  ; @return (hiccup)
  [blank-id blank-props]
  (let [content-props (components/extended-props->content-props blank-props)]
       [:div.x-blank (engine/element-attributes blank-id blank-props)
                     [components/content        blank-id content-props]
                     [engine/element-stickers   blank-id blank-props]]))

(defn element
  ; XXX#8711
  ; A blank elem az x.app-components.api/content komponens használatával jeleníti meg
  ; a számára :content tulajdonságként átadott tartalmat.
  ; A blank elemnél alkalmazott :content, :content-props és :subscriber tulajdonságok
  ; használatának leírását az x.app-components.api/content komponens dokumentációjában találod.
  ;
  ; @param (keyword)(opt) blank-id
  ; @param (map) blank-props
  ;  {:class (string or vector)(opt)
  ;   :content (metamorphic-content)(opt)
  ;   :content-props (map)(opt)
  ;   :stickers (maps in vector)(opt)
  ;    [{:disabled? (boolean)(opt)
  ;       Default: false
  ;      :icon (keyword)
  ;      :icon-family (keyword)(opt)
  ;       :material-icons-filled, :material-icons-outlined
  ;       Default: :material-icons-filled
  ;      :on-click (metamorphic-event)(opt)
  ;      :tooltip (metamorphic-content)(opt)}]
  ;   :style (map)(opt)
  ;   :subscriber (subscription vector)(opt)}
  ;
  ; @usage
  ;  XXX#7610
  ;
  ; @usage
  ;  [elements/blank {:content "My text"}]
  ;
  ; @usage
  ;  [elements/blank {:content :my-dictionary-term-id}]
  ;
  ; @usage
  ;  (defn my-component [_ _])
  ;  [elements/blank {:content #'my-component}]
  ;
  ; @usage
  ;  (defn my-component [_ subscribed-props])
  ;  [elements/blank {:content    #'my-component
  ;                   :subscriber [::get-subscribed-props]}]
  ;
  ; @usage
  ;  (defn my-component [component-id static-props subscribed-props])
  ;  [elements/blank :my-component {:content       #'my-component
  ;                                 :content-props {...}
  ;                                 :subscriber    [::get-subscribed-props]}]
  ;
  ; @return (component)
  ([blank-props]
   [element (a/id) blank-props])

  ([blank-id blank-props]
   (let [];blank-props (a/prot blank-props blank-props-prototype
        [blank blank-id blank-props])))
