
(ns elements.chip.views
    (:require [elements.chip.attributes :as chip.attributes]
              [elements.chip.prototypes :as chip.prototypes]
              [random.api               :as random]
              [x.components.api         :as x.components]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- chip
  ; @ignore
  ;
  ; @param (keyword) chip-id
  ; @param (map) chip-props
  ; {}
  [chip-id {:keys [icon icon-family label] :as chip-props}]
  [:div (chip.attributes/chip-attributes chip-id chip-props)
        [:div (chip.attributes/chip-body-attributes chip-id chip-props)
              (if-let [{:keys [icon icon-family]} (:primary-button chip-props)]
                      [:button (chip.attributes/primary-button-attributes chip-id chip-props)
                               [:i {:class :e-chip--primary-button-icon :data-icon-family icon-family} icon]]
                      [:div    {:class :e-chip--primary-button--placeholder}])
              (if icon  [:i {:class :e-chip--icon :data-icon-family icon-family :data-icon-size :xs} icon])
              (if label [:div {:class              :e-chip--label
                               :data-font-size     :xs
                               :data-font-weight   :medium
                               :data-line-height   :text-block
                               :data-text-overflow :no-wrap}
                              (x.components/content label)])]])

(defn element
  ; @param (keyword)(opt) chip-id
  ; @param (map) chip-props
  ; {:color (keyword or string)(opt)
  ;   :default, :highlight, :inherit, :invert, :muted, :primary, :secondary, :success, :warning
  ;   Default: :default
  ;  :class (keyword or keywords in vector)(opt)
  ;  :disabled? (boolean)(opt)
  ;  :fill-color (keyword or string)(opt)
  ;   :default, :highlight, :invert, :muted, :primary, :secondary, :success, :warning
  ;   Default: :primary
  ;  :href (string)(opt)
  ;   TODO Makes the chip clickable
  ;  :icon (keyword)(opt)
  ;  :icon-family (keyword)(opt)
  ;   :material-symbols-filled, :material-symbols-outlined
  ;   Default: :material-symbols-outlined
  ;  :indent (map)(opt)
  ;   {:bottom (keyword)(opt)
  ;    :left (keyword)(opt)
  ;    :right (keyword)(opt)
  ;    :top (keyword)(opt)
  ;    :horizontal (keyword)(opt)
  ;    :vertical (keyword)(opt)
  ;     :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl}
  ;  :label (metamorphic-content)
  ;  :on-click (Re-Frame metamorphic-event)(opt)
  ;   TODO Makes the chip clickable
  ;  :outdent (map)(opt)
  ;   Same as the :indent property
  ;  :primary-button (map)(opt)
  ;   {:icon (keyword)
  ;    :icon-family (keyword)(opt)
  ;     :material-symbols-filled, :material-symbols-outlined
  ;     Default: :material-symbols-outlined
  ;    :on-click (Re-Frame metamorphic-event)}
  ;  :style (map)(opt)
  ;  :target (keyword)(opt)
  ;   :blank, :self
  ;   TODO Makes the chip clickable}
  ;
  ; @usage
  ; [chip {...}]
  ;
  ; @usage
  ; [chip :my-chip {...}]
  ([chip-props]
   [element (random/generate-keyword) chip-props])

  ([chip-id chip-props]
   (let [chip-props (chip.prototypes/chip-props-prototype chip-props)]
        [chip chip-id chip-props])))
