
(ns pretty-elements.chip.views
    (:require [fruits.random.api                     :as random]
              [pretty-elements.adornment-group.views :as adornment-group.views]
              [pretty-elements.chip.attributes       :as chip.attributes]
              [pretty-elements.chip.prototypes       :as chip.prototypes]
              [pretty-elements.engine.api            :as pretty-elements.engine]
              [pretty-presets.engine.api             :as pretty-presets.engine]
              [pretty-accessories.api             :as pretty-accessories]
              [reagent.core :as reagent]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- chip
  ; @ignore
  ;
  ; @param (keyword) chip-id
  ; @param (map) chip-props
  ; {:cover (map)(opt)
  ;  :end-adornments (maps in vector)(opt)
  ;  :label (metamorphic-content)(opt)
  ;  :start-adornments (maps in vector)(opt)
  ;  ...}
  [chip-id {:keys [cover end-adornments label start-adornments] :as chip-props}]
  [:div (chip.attributes/chip-attributes chip-id chip-props)
        [(pretty-elements.engine/clickable-auto-tag chip-id chip-props)
         (chip.attributes/chip-body-attributes      chip-id chip-props)
         (when start-adornments [adornment-group.views/view chip-id {:adornments start-adornments}])
         (when :always          [:div (chip.attributes/chip-label-attributes chip-id chip-props) label])
         (when end-adornments   [adornment-group.views/view chip-id {:adornments end-adornments}])
         (when cover            [pretty-accessories/cover   chip-id cover])]])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- view-lifecycles
  ; @ignore
  ;
  ; @param (keyword) chip-id
  ; @param (map) chip-props
  [chip-id chip-props]
  ; @note (tutorials#parameterizing)
  ; @note (pretty-elements.adornment.views#8097)
  (reagent/create-class {:component-did-mount    (fn [_ _] (pretty-elements.engine/element-did-mount    chip-id chip-props))
                         :component-will-unmount (fn [_ _] (pretty-elements.engine/element-will-unmount chip-id chip-props))
                         :component-did-update   (fn [%]   (pretty-elements.engine/element-did-update   chip-id chip-props %))
                         :reagent-render         (fn [_ chip-props] [chip chip-id chip-props])}))

(defn view
  ; @description
  ; Optionally clickable chip style element with optional adornments.
  ;
  ; @links Implemented accessories
  ; [Cover](pretty-ui/cljs/pretty-accessories/api.html#cover)
  ; [Tooltip](pretty-ui/cljs/pretty-accessories/api.html#tooltip)
  ;
  ; @links Implemented elements
  ; [Adornment-group](pretty-ui/cljs/pretty-elements/api.html#adornment-group)
  ;
  ; @links Implemented properties
  ; [Anchor properties](pretty-core/cljs/pretty-properties/api.html#anchor-properties)
  ; [Background color properties](pretty-core/cljs/pretty-properties/api.html#background-color-properties)
  ; [Border properties](pretty-core/cljs/pretty-properties/api.html#border-properties)
  ; [Class properties](pretty-core/cljs/pretty-properties/api.html#class-properties)
  ; [Clickable state properties](pretty-core/cljs/pretty-properties/api.html#clickable-state-properties)
  ; [Cursor properties](pretty-core/cljs/pretty-properties/api.html#cursor-properties)
  ; [Effect properties](pretty-core/cljs/pretty-properties/api.html#effect-properties)
  ; [Font properties](pretty-core/cljs/pretty-properties/api.html#font-properties)
  ; [Keypress properties](pretty-core/cljs/pretty-properties/api.html#keypress-properties)
  ; [Label properties](pretty-core/cljs/pretty-properties/api.html#label-properties)
  ; [Lifecycle properties](pretty-core/cljs/pretty-properties/api.html#lifecycle-properties)
  ; [Mouse event properties](pretty-core/cljs/pretty-properties/api.html#mouse-event-properties)
  ; [Preset properties](pretty-core/cljs/pretty-properties/api.html#preset-properties)
  ; [Size properties](pretty-core/cljs/pretty-properties/api.html#size-properties)
  ; [Space properties](pretty-core/cljs/pretty-properties/api.html#space-properties)
  ; [Style properties](pretty-core/cljs/pretty-properties/api.html#style-properties)
  ; [Text properties](pretty-core/cljs/pretty-properties/api.html#text-properties)
  ; [Theme properties](pretty-core/cljs/pretty-properties/api.html#theme-properties)
  ;
  ; @param (keyword)(opt) chip-id
  ; @param (map) chip-props
  ; Check out the implemented accessories.
  ; Check out the implemented elements.
  ; Check out the implemented properties.
  ;
  ; @usage (chip.png)
  ; [chip {:border-radius         {:all :l}
  ;        :fill-color            :primary
  ;        :font-weight           :semi-bold
  ;        :gap                   :auto
  ;        :indent                {:left :xxs :right :s :vertical :xxs}
  ;        :label                 "My chip #1"
  ;        :width                 :xl
  ;        :end-adornment-default {:border-radius {:all :m} :fill-color :highlight}
  ;        :start-adornments      [{:icon :close :on-click-f (fn [_] ...)}]}]
  ;
  ; [chip {:border-color          :highlight
  ;        :border-radius         {:all :l}
  ;        :fill-color            :highlight
  ;        :font-weight           :semi-bold
  ;        :gap                   :auto
  ;        :indent                {:left :s :right :xxs :vertical :xxs}
  ;        :label                 "My chip #2"
  ;        :width                 :xl
  ;        :end-adornment-default {:border-color :highlight :border-radius {:all :m} :fill-color :default}
  ;        :end-adornments        [{:icon :close :on-click-f (fn [_] ...)}]}]
  ([chip-props]
   [view (random/generate-keyword) chip-props])

  ([chip-id chip-props]
   ; @note (tutorials#parameterizing)
   (fn [_ chip-props]
       (let [chip-props (pretty-presets.engine/apply-preset                            chip-id chip-props)
             chip-props (chip.prototypes/chip-props-prototype                          chip-id chip-props)
             chip-props (pretty-elements.engine/element-timeout-props                  chip-id chip-props)
             chip-props (pretty-elements.engine/element-subitem-group<-subitem-default chip-id chip-props :start-adornments :start-adornment-default)
             chip-props (pretty-elements.engine/element-subitem-group<-subitem-default chip-id chip-props :end-adornments   :end-adornment-default)
             chip-props (pretty-elements.engine/element-subitem-group<-disabled-state  chip-id chip-props :start-adornments)
             chip-props (pretty-elements.engine/element-subitem-group<-disabled-state  chip-id chip-props :end-adornments)]
            [view-lifecycles chip-id chip-props]))))
