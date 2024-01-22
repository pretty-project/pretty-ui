
(ns pretty-inputs.chip-group.views
    (:require [fruits.random.api                   :as random]
              [fruits.vector.api                   :as vector]
              [fruits.hiccup.api                   :as hiccup]
              [metamorphic-content.api             :as metamorphic-content]
              [pretty-elements.api                 :as pretty-elements]
              [pretty-inputs.chip-group.attributes :as chip-group.attributes]
              [pretty-inputs.chip-group.prototypes :as chip-group.prototypes]
              [pretty-inputs.core.side-effects :as core.side-effects]
              [pretty-inputs.core.env :as core.env]
              [pretty-inputs.core.views            :as core.views]
              [pretty-presets.api                  :as pretty-presets]
              [reagent.api :as reagent]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- chip-group-chip-list
  ; @ignore
  ;
  ; @param (keyword) group-id
  ; @param (map) group-props
  ; {:placeholder (metamorphic-content)(opt)}
  [group-id {:keys [placeholder] :as group-props}]
  (let [chips (core.env/get-input-displayed-value group-id group-props)]
       (if (vector/not-empty? chips)
           (letfn [(f0 [chip-dex %] (let [chip-props (chip-group.prototypes/chip-props-prototype group-id group-props chip-dex %)]
                                         [pretty-elements/chip chip-props]))]
                  (hiccup/put-with-indexed [:<>] chips f0))
           (if placeholder [:div (chip-group.attributes/chip-group-placeholder-attributes group-id group-props)
                                 (metamorphic-content/compose placeholder)]))))

(defn- chip-group
  ; @ignore
  ;
  ; @param (keyword) group-id
  ; @param (map) group-props
  [group-id group-props]
  [:div (chip-group.attributes/chip-group-attributes group-id group-props)
        [core.views/input-label                      group-id group-props]
        [:div (chip-group.attributes/chip-group-body-attributes group-id group-props)
              [chip-group-chip-list                             group-id group-props]]])

(defn- chip-group-lifecycles
  ; @ignore
  ;
  ; @param (keyword) group-id
  ; @param (map) group-props
  [group-id group-props]
  ; @note (tutorials#parametering)
  (reagent/lifecycles {:component-did-mount    (fn [_ _] (core.side-effects/input-did-mount    group-id group-props))
                       :component-will-unmount (fn [_ _] (core.side-effects/input-will-unmount group-id group-props))
                       :reagent-render         (fn [_ group-props] [chip-group group-id group-props])}))

(defn input
  ; @param (keyword)(opt) group-id
  ; @param (map) group-props
  ; {:class (keyword or keywords in vector)(opt)
  ;  :chip (map)(opt)
  ;  :chip-label-f (function)(opt)
  ;  :chips-deletable? (boolean)(opt)
  ;  :get-value-f (function)(opt)
  ;  :helper (metamorphic-content)(opt)
  ;  :indent (map)(opt)
  ;   {:all, :bottom, :left, :right, :top, :horizontal, :vertical (keyword, px or string)(opt)}
  ;  :info-text (metamorphic-content)(opt)
  ;  :initial-value (vector)(opt)
  ;  :label (metamorphic-content)(opt)
  ;  :on-empty-f (function)(opt)
  ;  :on-mount-f (function)(opt)
  ;  :on-unmount-f (function)(opt)
  ;  :outdent (map)(opt)
  ;   {:all, :bottom, :left, :right, :top, :horizontal, :vertical (keyword, px or string)(opt)}
  ;  :placeholder (metamorphic-content)(opt)
  ;  :preset (keyword)(opt)
  ;  :projected-value (vector)(opt)
  ;  :set-value-f (function)(opt)
  ;  :style (map)(opt)}
  ;
  ; @usage
  ; [chip-group {...}]
  ;
  ; @usage
  ; [chip-group :my-chip-group {...}]
  ;
  ; @usage
  ; [chip-group :my-chip-group {:chip {:fill-color :primary :indent {:all :xs} ...}
  ;                             :chip-label-f  :label
  ;                             :placeholder   "No chips displayed"
  ;                             :initial-value [{:label "Chip #1"} {:label "Chip #2"}]
  ;                             :get-value-f   #(deref  my-atom)
  ;                             :set-value-f   #(reset! my-atom %)}]
  ([group-props]
   [input (random/generate-keyword) group-props])

  ([group-id group-props]
   ; @note (tutorials#parametering)
   (fn [_ group-props]
       (let [group-props (pretty-presets/apply-preset                 group-props)
             group-props (chip-group.prototypes/group-props-prototype group-props)]
            [chip-group-lifecycles group-id group-props]))))
