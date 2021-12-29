
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.08.19
; Description:
; Version: v0.4.6
; Compatibility: x4.4.8



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-elements.expandable
    (:require [mid-fruits.candy          :refer [param]]
              [mid-fruits.logical        :refer [nonfalse?]]
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



;; -- Subscriptions -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- get-expandable-props
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) expandable-id
  ;
  ; @return (map)
  [db [_ expandable-id]]
  (merge (r engine/get-element-props    db expandable-id)
         (r engine/get-expandable-props db expandable-id)))

(a/reg-sub :elements/get-expandable-props get-expandable-props)



;; -- Components --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- expandable-expand-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) expandable-id
  ; @param (map) expandable-props
  ;  {:expanded? (boolean)}
  ;
  ; @return (hiccup)
  [expandable-id {:keys [expanded?]}]
  (if (nonfalse? expanded?)
      [:i.x-expandable--expand-icon :expand_less]
      [:i.x-expandable--expand-icon :expand_more]))

(defn- expandable-icon
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) expandable-id
  ; @param (map) expandable-props
  ;  {:icon (keyword)(opt)}
  ;
  ; @return (hiccup)
  [_ {:keys [icon]}]
  (if (some? icon)
      [:i.x-expandable--icon icon]))

(defn- expandable-label
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) expandable-id
  ; @param (map) expandable-props
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
  ; @param (map) expandable-props
  ;
  ; @return (hiccup)
  [expandable-id expandable-props]
  [:button.x-expandable--header {:on-click   #(a/dispatch [:elements/toggle-element-expansion! expandable-id])
                                 :on-mouse-up (engine/blur-element-function expandable-id)}
                                [expandable-icon          expandable-id expandable-props]
                                [expandable-label         expandable-id expandable-props]
                                [expandable-expand-button expandable-id expandable-props]])

(defn- expandable-body
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) expandable-id
  ; @param (map) expandable-props
  ;  {:expanded? (boolean)}
  ;
  ; @return (hiccup)
  [expandable-id {:keys [expanded?] :as expandable-props}]
  (if expanded? (let [content-props (components/extended-props->content-props expandable-props)]
                     [:div.x-expandable--body [components/content expandable-id content-props]])))

(defn expandable
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) expandable-id
  ; @param (map) expandable-props
  ;
  ; @return (hiccup)
  [expandable-id expandable-props]
  [:div.x-expandable (engine/element-attributes expandable-id expandable-props)
                     [expandable-header         expandable-id expandable-props]
                     [expandable-body           expandable-id expandable-props]])

(defn element
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
  ;   :indent (keyword)(opt)
  ;    :left, :right, :both, :none
  ;    Default: :none
  ;   :label (metamorphic-content)(opt)
  ;   :layout (keyword)(opt)
  ;    :fit, :row
  ;    Default: :row
  ;   :subscriber (subscription-vector)(opt)}
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
   [element (a/id) expandable-props])

  ([expandable-id expandable-props]
   (let [expandable-props (expandable-props-prototype expandable-props)]
        [engine/stated-element expandable-id
                               {:component     #'expandable
                                :element-props expandable-props
                                :subscriber    [:elements/get-expandable-props expandable-id]}])))
