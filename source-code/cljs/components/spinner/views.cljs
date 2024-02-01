
(ns components.spinner.views
    (:require [components.spinner.prototypes :as spinner.prototypes]
              [fruits.random.api             :as random]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- spinner
  ; @param (keyword) spinner-id
  ; @param (map) spinner-props
  ; {}
  [_ {:keys [color size]}]
  [:div {:class :c-spinner}])

(defn view
  ; @param (keyword)(opt) spinner-id
  ; @param (map) spinner-props
  ; {:color (keyword)(opt)
  ;   :default, :highlight, :invert, :muted, :primary, :secondary, :success, :warning
  ;   Default: :primary
  ;  :size (keyword)(opt)
  ;   :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl
  ;   Default: :m}
  ;
  ; @usage
  ; [spinner {...}]
  ;
  ; @usage
  ; [spinner :my-spinner {...}]
  ([spinner-props]
   [view (random/generate-keyword) spinner-props])

  ([spinner-id spinner-props]
   ; @note (tutorials#parametering)
   (fn [_ spinner-props]
       (let [spinner-props (spinner.prototypes/spinner-props-prototype spinner-props)]
            [spinner spinner-id spinner-props]))))
