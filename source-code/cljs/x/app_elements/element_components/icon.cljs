
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-elements.element-components.icon
    (:require [mid-fruits.candy          :refer [param]]
              [x.app-core.api            :as a]
              [x.app-elements.engine.api :as engine]))



;; -- Prototypes --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- icon-props-prototype
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) icon-props
  ;
  ; @return (map)
  ;  {:icon-family
  ;   :size (keyword)}
  [icon-props]
  (merge {:icon-family :material-icons-filled
          :size        :m}
         (param icon-props)))



;; -- Components --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- icon
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) icon-id
  ; @param (map) icon-props
  ;  {:icon (keyword)}
  [icon-id {:keys [icon] :as icon-props}]
  [:div.x-icon (engine/element-attributes icon-id icon-props)
               [:i.x-icon--body icon]])

(defn element
  ; @param (keyword)(opt) icon-id
  ; @param (map) icon-props
  ;  {:class (keyword or keywords in vector)(opt)
  ;   :color (keyword or string)(opt)
  ;    :default, :highlight, :invert, :muted, :primary, :secondary, :success, :warning
  ;    Default: :default
  ;   :icon (keyword)
  ;   :icon-family (keyword)(opt)
  ;    :material-icons-filled, :material-icons-outlined
  ;    Default: :material-icons-filled
  ;    Only w/ {:icon ...}
  ;   :indent (map)(opt)
  ;    {:bottom (keyword)(opt)
  ;      :xxs, :xs, :s, :m, :l, :xl, :xxl
  ;     :left (keyword)(opt)
  ;      :xxs, :xs, :s, :m, :l, :xl, :xxl
  ;     :right (keyword)(opt)
  ;      :xxs, :xs, :s, :m, :l, :xl, :xxl
  ;     :top (keyword)(opt)
  ;      :xxs, :xs, :s, :m, :l, :xl, :xxl}
  ;   :layout (keyword)(opt)
  ;    :touch-target Az ikont tartalmazó elem méretei megegyeznek az icon-button típus méreteivel
  ;   :size (keyword)(opt)
  ;    :xxs, :xs, :s, :m, :l, :xl, :xxl
  ;    Default: :m
  ;   :style (map)(opt)}
  ;
  ; @usage
  ;  [elements/icon {...}]
  ;
  ; @usage
  ;  [elements/icon :my-icon {...}]
  ([icon-props]
   [element (a/id) icon-props])

  ([icon-id icon-props]
   (let [icon-props (icon-props-prototype icon-props)]
        [icon icon-id icon-props])))
