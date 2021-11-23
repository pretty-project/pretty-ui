
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.08.19
; Description:
; Version: v0.2.8
; Compatibility: x4.4.2



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-elements.expandable
    (:require [mid-fruits.candy          :refer [param]]
              [mid-fruits.css            :as css]
              [mid-fruits.keyword        :as keyword]
              [mid-fruits.logical        :refer [nonfalse?]]
              [mid-fruits.math           :as math]
              [mid-fruits.svg            :as svg]
              [mid-fruits.vector         :as vector]
              [x.app-components.api      :as components]
              [x.app-core.api            :as a :refer [r]]
              [x.app-elements.engine.api :as engine]))



;; -- Prototypes --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn expandable-props-prototype
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) expandable-props
  ;  {:icon (keyword)}
  ;
  ; @return (map)
  ;  {:expanded? (boolean)
  ;   :row (keyword)}
  [{:keys [icon] :as expandable-props}]
  (merge {:expanded? false
          :layout    :row}
         (if (some? icon) {:icon-family :material-icons-filled})
         (param expandable-props)))



;; -- Components --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- expandable-expand-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) expandable-id
  ; @param (map) view-props
  ;  {:expanded? (boolean)}
  ;
  ; @return (hiccup)
  [expandable-id {:keys [expanded?]}]
  (if (nonfalse? expanded?)
      [:i.x-expandable--expand-icon (keyword/to-dom-value :expand_less)]
      [:i.x-expandable--expand-icon (keyword/to-dom-value :expand_more)]))

(defn- expandable-icon
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) expandable-id
  ; @param (map) view-props
  ;  {:icon (keyword)(opt)}
  ;
  ; @return (hiccup)
  [_ {:keys [icon]}]
  (if (some? icon)
      [:i.x-expandable--icon (keyword/to-dom-value icon)]))

(defn- expandable-label
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) expandable-id
  ; @param (map) view-props
  ;  {:label (metamorphic-content)(opt)}
  ;
  ; @return (hiccup)
  [_ {:keys [label]}]
  (if (some? label)
      [:div.x-expandable--label [components/content {:content label}]]))

(defn- expandable-header
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) expandable-id
  ; @param (map) view-props
  ;
  ; @return (hiccup)
  [expandable-id view-props]
  [:button.x-expandable--header
    {:on-click   #(a/dispatch [:x.app-elements/toggle-element-expansion! expandable-id])
     :on-mouse-up (engine/blur-element-function expandable-id)}
    [expandable-icon          expandable-id view-props]
    [expandable-label         expandable-id view-props]
    [expandable-expand-button expandable-id view-props]])

(defn- expandable-body
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) expandable-id
  ; @param (map) view-props
  ;  {:expanded? (boolean)}
  ;
  ; @return (hiccup)
  [expandable-id {:keys [expanded?] :as view-props}]
  (if (boolean expanded?)
      (let [content-props (components/extended-props->content-props view-props)]
           [:div.x-expandable--body [components/content expandable-id content-props]])))

(defn expandable
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) expandable-id
  ; @param (map) view-props
  ;
  ; @return (hiccup)
  [expandable-id view-props]
  [:div.x-expandable
    (engine/element-attributes expandable-id view-props)
    [expandable-header expandable-id view-props]
    [expandable-body   expandable-id view-props]])

(defn view
  ; XXX#8711
  ; Az expandable elem az x.app-components.api/content komponens használatával jeleníti meg
  ; a számára :content tulajdonságként átadott tartalmat.
  ; Az expandable elemnél alkalmazott :content, :content-props és :subscriber tulajdonságok
  ; használatának leírását az x.app-components.api/content komponens dokumentációjában találod.
  ;
  ; @param (keyword)(opt) expandable-id
  ; @param (map) expandable-props
  ;  {:content (metamorphic-content)
  ;   :content-props (map)(opt)
  ;   :expanded? (boolean)(opt)
  ;    Default: true
  ;   :icon (keyword)(opt)
  ;   :icon-family (keyword)(opt)
  ;    :material-icons-filled, :material-icons-outlined
  ;    Default: :material-icons-filled
  ;    Only w/ {:icon ...}
  ;   :label (metamorphic-content)(opt)
  ;   :layout (keyword)(opt)
  ;    :fit, :row
  ;    Default: :row
  ;   :subscriber (subscription vector)(opt)}
  ;
  ; @usage
  ;  XXX#7610
  ;  Az expandable elemen megjelenő tartalom használatának leírását a blank elem dokumentációjában találod.
  ;
  ; @usage
  ;  [elements/expandable {...}]
  ;
  ; @usage
  ;  [elements/expandable :my-expandable {...}]
  ;
  ; @return (component)
  ([expandable-props]
   [view nil expandable-props])

  ([expandable-id expandable-props]
   (let [expandable-id    (a/id   expandable-id)
         expandable-props (a/prot expandable-props expandable-props-prototype)]
        [expandable expandable-id expandable-props])))
