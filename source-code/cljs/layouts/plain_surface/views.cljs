
(ns layouts.plain-surface.views
    (:require [layouts.plain-surface.attributes :as plain-surface.attributes]
              [layouts.plain-surface.prototypes :as plain-surface.prototypes]
              [random.api                       :as random]
              [re-frame.api                     :as r]
              [reagent.api                      :as reagent]
              [x.components.api                 :as x.components]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- plain-surface-structure
  ; @ignore
  ;
  ; @param (keyword) surface-id
  ; @param (map) surface-props
  ; {:content (metamorphic-content)}
  [surface-id {:keys [content] :as surface-props}]
  [:div {:class :l-plain-surface}
        [:div (plain-surface.attributes/surface-body-attributes surface-id surface-props)
              [x.components/content                             surface-id content]]])

(defn- plain-surface
  ; @ignore
  ;
  ; @param (keyword) surface-id
  ; @param (map) surface-props
  ; {}
  [surface-id {:keys [on-mount on-unmount] :as surface-props}]
  (reagent/lifecycles {:component-did-mount    (fn [_ _] (r/dispatch on-mount))
                       :component-will-unmount (fn [_ _] (r/dispatch on-unmount))
                       :reagent-render         (fn [_ _] [plain-surface-structure surface-id surface-props])}))

(defn layout
  ; @param (keyword)(opt) surface-id
  ; @param (map) surface-props
  ; {:content (metamorphic-content)
  ;  :content-orientation (keyword)(opt)
  ;   :horizontal, :vertical
  ;   Default: :vertical
  ;  :fill-color (keyword or string)(opt)
  ;   :default, :highlight, :invert, :muted, :primary, :secondary, :success, :warning
  ;  :on-mount (metamorphic-event)(opt)
  ;  :on-unmount (metamorphic-event)(opt)
  ;  :style (map)(opt)}
  ;
  ; @usage
  ; [plain-surface {...}]
  ;
  ; @usage
  ; [plain-surface :my-plain-surface {...}]
  ([surface-props]
   [layout (random/generate-keyword) surface-props])

  ([surface-id surface-props]
   (let [layout-props (plain-surface.prototypes/surface-props-prototype surface-props)]
        [plain-surface surface-id surface-props])))
