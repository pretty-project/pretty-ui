
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-elements.element-components.expandable
    (:require [mid-fruits.candy                   :refer [param]]
              [mid-fruits.logical                 :refer [nonfalse?]]
              [x.app-components.api               :as components]
              [x.app-core.api                     :as a :refer [r]]
              [x.app-environment.api              :as environment]
              [x.app-elements.engine.api          :as engine]
              [x.app-elements.expand-handler.subs :as expand-handler.subs]))



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
         (if icon {:icon-family :material-icons-filled})
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
  (merge (r engine/get-element-props                 db expandable-id)
         (r expand-handler.subs/get-expandable-props db expandable-id)))

(a/reg-sub :elements/get-expandable-props get-expandable-props)



;; -- Components --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- expandable-expand-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) expandable-id
  ; @param (map) expandable-props
  ;  {:expanded? (boolean)}
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
  [_ {:keys [icon]}]
  (if icon [:i.x-expandable--icon icon]))

(defn- expandable-label
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) expandable-id
  ; @param (map) expandable-props
  ;  {:label (metamorphic-content)(opt)}
  [_ {:keys [label]}]
  (if label [:div.x-expandable--label [components/content label]]))

(defn- expandable-header
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) expandable-id
  ; @param (map) expandable-props
  [expandable-id expandable-props]
  [:button.x-expandable--header {:on-click    #(a/dispatch [:elements/toggle-element-expansion! expandable-id])
                                 :on-mouse-up #(environment/blur-element!)}
                                [expandable-icon          expandable-id expandable-props]
                                [expandable-label         expandable-id expandable-props]
                                [expandable-expand-button expandable-id expandable-props]])

(defn- expandable-body
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) expandable-id
  ; @param (map) expandable-props
  ;  {:content (metamorphic-content)(opt)
  ;   :expanded? (boolean)}
  [expandable-id {:keys [content expanded?]}]
  (if expanded? [:div.x-expandable--body [components/content expandable-id content]]))

(defn expandable
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) expandable-id
  ; @param (map) expandable-props
  [expandable-id expandable-props]
  [:div.x-expandable (engine/element-attributes expandable-id expandable-props)
                     [expandable-header         expandable-id expandable-props]
                     [expandable-body           expandable-id expandable-props]])

(defn element
  ; @param (keyword)(opt) expandable-id
  ; @param (map) expandable-props
  ;  {:content (metamorphic-content)
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
  ;    Default: :row}
  ;
  ; @usage
  ;  [elements/expandable {...}]
  ;
  ; @usage
  ;  [elements/expandable :my-expandable {...}]
  ([expandable-props]
   [element (a/id) expandable-props])

  ([expandable-id expandable-props]
   (let [expandable-props (expandable-props-prototype expandable-props)]
        [engine/stated-element expandable-id
                               {:render-f      #'expandable
                                :element-props expandable-props
                                :subscriber    [:elements/get-expandable-props expandable-id]}])))
