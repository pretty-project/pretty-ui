
(ns pretty-elements.horizontal-group.views
    (:require [fruits.hiccup.api                           :as hiccup]
              [fruits.random.api                           :as random]
              [pretty-elements.horizontal-group.attributes :as horizontal-group.attributes]
              [pretty-elements.horizontal-group.prototypes :as horizontal-group.prototypes]
              [pretty-elements.engine.api                           :as pretty-elements.engine]
              [pretty-presets.engine.api :as pretty-presets.engine]
              [reagent.api                                 :as reagent]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- horizontal-group
  ; @ignore
  ;
  ; @param (keyword) group-id
  ; @param (map) group-props
  [group-id {:keys [default-props element group-items] :as group-props}]
  [:div (horizontal-group.attributes/group-attributes group-id group-props)
        [:div (horizontal-group.attributes/group-body-attributes group-id group-props)
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
                       :reagent-render         (fn [_ group-props] [horizontal-group group-id group-props])}))

(defn view
  ; @param (keyword)(opt) group-id
  ; @param (map) group-props
  ; {:class (keyword or keywords in vector)(opt)
  ;  :default-props (map)(opt)
  ;  :disabled? (boolean)(opt)
  ;  :element (Reagent component symbol)
  ;  :group-items (maps in vector)
  ;  :height (keyword, px or string)(opt)
  ;  :indent (map)(opt)
  ;   {:all, :bottom, :left, :right, :top, :horizontal, :vertical (keyword, px or string)(opt)}
  ;  :on-mount-f (function)(opt)
  ;  :on-unmount-f (function)(opt)
  ;  :outdent (map)(opt)
  ;   {:all, :bottom, :left, :right, :top, :horizontal, :vertical (keyword, px or string)(opt)}
  ;  :preset (keyword)(opt)
  ;  :style (map)(opt)
  ;  :theme (keyword)(opt)
  ;  :width (keyword, px or string)(opt)}
  ;
  ; @usage
  ; [horizontal-group {...}]
  ;
  ; @usage
  ; [horizontal-group :my-horizontal-group {...}]
  ;
  ; @usage
  ; [horizontal-group {:default-props {:hover-color :highlight}
  ;                    :element #'elements.api/button
  ;                    :group-items [{:label "First button"  :href "/first"}
  ;                                  {:label "Second button" :href "/second"}]}]
  ([group-props]
   [view (random/generate-keyword) group-props])

  ([group-id group-props]
   ; @note (tutorials#parameterizing)
   (fn [_ group-props]
       (let [group-props (pretty-presets.engine/apply-preset                group-id group-props)
             group-props (horizontal-group.prototypes/group-props-prototype group-id group-props)]
            [view-lifecycles group-id group-props]))))
