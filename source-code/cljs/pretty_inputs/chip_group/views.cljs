
(ns pretty-inputs.chip-group.views
    (:require [fruits.hiccup.api                   :as hiccup]
              [fruits.random.api                   :as random]
              [fruits.vector.api                   :as vector]
              [metamorphic-content.api             :as metamorphic-content]
              [pretty-elements.api                 :as pretty-elements]
              [pretty-inputs.engine.api                   :as pretty-inputs.engine]
              [pretty-inputs.chip-group.attributes :as chip-group.attributes]
              [pretty-inputs.chip-group.prototypes :as chip-group.prototypes]
              [pretty-presets.engine.api :as pretty-presets.engine]
              [reagent.api                         :as reagent]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- chip-group-chip
  ; @ignore
  ;
  ; @param (keyword) group-id
  ; @param (map) group-props
  ; @param (integer) chip-dex
  ; @param (*) chip-value
  [group-id group-props chip-dex chip-value]
  (let [chip-props (chip-group.prototypes/chip-props-prototype group-id group-props chip-dex chip-value)]
       [pretty-elements/chip chip-props]))

(defn- chip-group-chip-list
  ; @ignore
  ;
  ; @param (keyword) group-id
  ; @param (map) group-props
  ; {:placeholder (metamorphic-content)(opt)}
  [group-id {:keys [placeholder] :as group-props}]
  (letfn [(f0 [chip-dex chip-value] [chip-group-chip group-id group-props chip-dex chip-value])]
         (let [chips (pretty-inputs.engine/get-input-displayed-value group-id group-props)]
              (cond (-> chips vector/not-empty?) (hiccup/put-with-indexed [:<>] chips f0)
                    (-> placeholder) [:div (chip-group.attributes/chip-group-placeholder-attributes group-id group-props)
                                           (metamorphic-content/compose placeholder)]))))

(defn- chip-group
  ; @ignore
  ;
  ; @param (keyword) group-id
  ; @param (map) group-props
  [group-id group-props]
  [:div (chip-group.attributes/chip-group-attributes group-id group-props)
        (if-let [label-props (pretty-inputs.engine/input-label-props group-id group-props)]
                [pretty-elements/label label-props])
        [pretty-inputs.engine/input-synchronizer group-id group-props]
        [:div (chip-group.attributes/chip-group-body-attributes group-id group-props)
              [chip-group-chip-list                             group-id group-props]]])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- input-lifecycles
  ; @ignore
  ;
  ; @param (keyword) group-id
  ; @param (map) group-props
  [group-id group-props]
  ; @note (tutorials#parametering)
  (reagent/lifecycles {:component-did-mount    (fn [_ _] (pretty-inputs.engine/input-did-mount    group-id group-props))
                       :component-will-unmount (fn [_ _] (pretty-inputs.engine/input-will-unmount group-id group-props))
                       :reagent-render         (fn [_ group-props] [chip-group group-id group-props])}))

(defn input
  ; @param (keyword)(opt) group-id
  ; @param (map) group-props
  ; {:class (keyword or keywords in vector)(opt)
  ;  :chip (map)(opt)
  ;  :chip-label-f (function)(opt)
  ;  :chips-unselectable? (boolean)(opt)
  ;  :get-value-f (function)(opt)
  ;  :helper (metamorphic-content)(opt)
  ;  :indent (map)(opt)
  ;   {:all, :bottom, :left, :right, :top, :horizontal, :vertical (keyword, px or string)(opt)}
  ;  :info-text (metamorphic-content)(opt)
  ;  :initial-value (vector)(opt)
  ;  :label (metamorphic-content)(opt)
  ;  :on-changed-f (function)(opt)
  ;  :on-empty-f (function)(opt)
  ;  :on-mount-f (function)(opt)
  ;  :on-unmount-f (function)(opt)
  ;  :on-unselected-f (function)(opt)
  ;  :outdent (map)(opt)
  ;   {:all, :bottom, :left, :right, :top, :horizontal, :vertical (keyword, px or string)(opt)}
  ;  :placeholder (metamorphic-content)(opt)
  ;  :preset (keyword)(opt)
  ;  :projected-value (vector)(opt)
  ;  :set-value-f (function)(opt)
  ;  :style (map)(opt)
  ;  :theme (keyword)(opt)}
  ;
  ; @usage
  ; [chip-group {...}]
  ;
  ; @usage
  ; [chip-group :my-chip-group {...}]
  ([group-props]
   [input (random/generate-keyword) group-props])

  ([group-id group-props]
   ; @note (tutorials#parametering)
   (fn [_ group-props]
       (let [group-props (pretty-presets.engine/apply-preset          group-props)
             group-props (chip-group.prototypes/group-props-prototype group-props)]
            [input-lifecycles group-id group-props]))))
