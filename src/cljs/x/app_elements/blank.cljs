
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.02.22
; Description:
; Version: v0.1.2
; Compatibility: x3.9.9



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
  ;  {:request-id (keyword)(opt)}
  ;
  ; @return (map)
  ;  {:status-animation? (boolean)}
  [{:keys [request-id] :as blank-props}]
  (merge {}
         (if (some? request-id) {:status-animation? true})
         (param blank-props)))



;; -- Subscriptions -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- get-view-props
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) blank-id
  ;
  ; @return (map)
  [db [_ blank-id]]
  (r engine/get-element-view-props db blank-id))

(a/reg-sub ::get-view-props get-view-props)



;; -- Components --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- blank
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) blank-id
  ; @param (map) view-props
  ;  {:content (metamorphic-content)}
  ;
  ; @return (hiccup)
  [blank-id view-props]
  (let [content-props (components/extended-props->content-props view-props)]
       [:div.x-blank
         (engine/element-attributes blank-id view-props)
         [components/content blank-id content-props]]))

(defn view
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
  ;   :request-id (keyword)(constant)(opt)
  ;   :status-animation? (boolean)(opt)
  ;    Default: true
  ;    Only w/ {:request-id ...}
  ;   :stickers (maps in vector)(opt)
  ;    [{:disabled? (boolean)(opt)
  ;       Default: false
  ;      :icon (keyword) Material icon class
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
  ;  (defn my-component [_ view-props])
  ;  [elements/blank {:content    #'my-component
  ;                   :subscriber [::get-view-props]}]
  ;
  ; @usage
  ;  (defn my-component [component-id content-props view-props])
  ;  [elements/blank :my-component {:content       #'my-component
  ;                                 :content-props {...}
  ;                                 :subscriber    [::get-view-props]}]
  ;
  ; @return (component)
  ([blank-props]
   [view nil blank-props])

  ([blank-id blank-props]
   (let [blank-id    (a/id   blank-id)
         blank-props (a/prot blank-props blank-props-prototype)]
        [engine/container blank-id
          {:base-props blank-props
           :component  blank
           :subscriber [::get-view-props blank-id]}])))
