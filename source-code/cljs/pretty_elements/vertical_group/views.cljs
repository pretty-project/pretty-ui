
(ns pretty-elements.vertical-group.views
    (:require [fruits.hiccup.api                         :as hiccup]
              [fruits.random.api                         :as random]
              [pretty-elements.vertical-group.attributes :as vertical-group.attributes]
              [pretty-elements.vertical-group.prototypes :as vertical-group.prototypes]
              [pretty-elements.engine.api                         :as pretty-elements.engine]
              [pretty-presets.engine.api :as pretty-presets.engine]
              [reagent.api                               :as reagent]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- vertical-group
  ; @ignore
  ;
  ; @param (keyword) group-id
  ; @param (map) group-props
  [group-id {:keys [default-props element group-items] :as group-props}]
  [:div (vertical-group.attributes/group-attributes group-id group-props)
        [:div (vertical-group.attributes/group-body-attributes group-id group-props)
              (letfn [(f0 [group-item] [element (merge default-props group-item)])]
                     (hiccup/put-with [:<>] group-items f0))]])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- view-lifecycles
  ; @ignore
  ;
  ; @param (keyword) group-id
  ; @param (map) group-props
  [group-id group-props]
  ; @note (tutorials#parameterizing)
  (reagent/lifecycles {:component-did-mount    (fn [_ _] (pretty-elements.engine/element-did-mount    group-id group-props))
                       :component-will-unmount (fn [_ _] (pretty-elements.engine/element-will-unmount group-id group-props))
                       :reagent-render         (fn [_ group-props] [vertical-group group-id group-props])}))

(defn view
  ; @param (keyword)(opt) group-id
  ; @param (map) group-props
  ; {:class (keyword or keywords in vector)(opt)
  ;  :default-props (map)(opt)
  ;  :disabled? (boolean)(opt)
  ;  :element (Reagent component symbol)
  ;  :group-item-default (map)(opt)
  ;  :group-items (maps in vector)(opt)
  ;  :height (keyword, px or string)(opt)
  ;   Default: :parent
  ;  :indent (map)(opt)
  ;   {:all, :bottom, :left, :right, :top, :horizontal, :vertical (keyword, px or string)(opt)}
  ;  :on-mount-f (function)(opt)
  ;  :on-unmount-f (function)(opt)
  ;  :outdent (map)(opt)
  ;   {:all, :bottom, :left, :right, :top, :horizontal, :vertical (keyword, px or string)(opt)}
  ;  :preset (keyword)(opt)
  ;  :style (map)(opt)
  ;  :theme (keyword)(opt)
  ;  :width (keyword, px or string)(opt)
  ;   Default: :auto}
  ;
  ; @usage
  ; [vertical-group {...}]
  ;
  ; @usage
  ; [vertical-group :my-vertical-group {...}]
  ;
  ; @usage
  ; [vertical-group {:default-props {:hover-color :highlight}
  ;                  :element #'elements.api/button
  ;                  :group-items [{:label "First button"  :href "/first"}
  ;                                {:label "Second button" :href "/second"}]}]
  ([group-props]
   [view (random/generate-keyword) group-props])

  ([group-id group-props]
   ; @note (tutorials#parameterizing)
   (fn [_ group-props]
       (let [group-props (pretty-presets.engine/apply-preset                            group-id group-props)
             group-props (vertical-group.prototypes/group-props-prototype               group-id group-props)
             group-props (pretty-elements.engine/element-subitem-group<-subitem-default group-id group-props :group-items :group-item-default)
             group-props (pretty-elements.engine/element-subitem-group<-disabled-state  group-id group-props :group-items :group-item-default)
             group-props (pretty-elements.engine/dissoc-element-disabled-state          group-id group-props)]
            [view-lifecycles group-id group-props]))))
