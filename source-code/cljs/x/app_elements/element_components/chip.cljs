
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-elements.element-components.chip
    (:require [mid-fruits.candy                         :refer [param]]
              [x.app-components.api                     :as components]
              [x.app-core.api                           :as a :refer [r]]
              [x.app-elements.deletable-elements.engine :as deletable-elements.engine]
              [x.app-elements.engine.api                :as engine]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; x.app-elements.deletable-elements.engine
(def deletable-body-attributes x.app-elements.deletable-elements.engine/deletable-body-attributes)



;; -- Prototypes --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn chip-props-prototype
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) chip-props
  ;  {:icon (keyword)(opt)}
  ;
  ; @return (map)
  ;  {:background-color (keyword)
  ;   :delete-button-icon (keyword)
  ;   :icon-family (keyword)
  ;   :layout (keyword)}
  [{:keys [icon] :as chip-props}]
  (merge {:background-color   :primary
          :layout             :row
          :delete-button-icon :close}
         (if icon {:icon-family :material-icons-filled})
         (param chip-props)))



;; -- Components --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- chip-icon
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) chip-id
  ; @param (map) chip-props
  ;  {:icon (keyword)(opt)}
  [_ {:keys [icon]}]
  (if icon [:i.x-chip--icon icon]))

(defn- chip-label
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) chip-id
  ; @param (map) chip-props
  ;  {:label (metamorphic-content)}
  [_ {:keys [label]}]
  [:div.x-chip--label [components/content label]])

(defn- chip-delete-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) chip-id
  ; @param (map) chip-props
  ;  {:delete-button-icon (keyword)
  ;   :on-delete (metamorphic-event)(opt)}
  [chip-id {:keys [delete-button-icon on-delete] :as chip-props}]
  (if on-delete [:button.x-chip--delete-button (deletable-body-attributes     chip-id chip-props)
                                               [:i.x-chip--delete-button-icon delete-button-icon]]))

(defn- chip-body
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) chip-id
  ; @param (map) chip-props
  [chip-id chip-props]
  [:div.x-chip--body [chip-icon          chip-id chip-props]
                     [chip-label         chip-id chip-props]
                     [chip-delete-button chip-id chip-props]])

(defn- chip
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) chip-id
  ; @param (map) chip-props
  [chip-id chip-props]
  [:div.x-chip (engine/element-attributes chip-id chip-props)
               [chip-body                 chip-id chip-props]])

(defn element
  ; @param (keyword)(opt) chip-id
  ; @param (map) chip-props
  ;  XXX#7701
  ;  {:background-color (keyword)(opt)
  ;    :highlight, :muted, :primary, :secondary, :success, :warning
  ;    Default: :primary
  ;   :class (keyword or keywords in vector)(opt)
  ;   :delete-button-icon (keyword)(opt)
  ;    Default: :close
  ;   :disabled? (boolean)(opt)
  ;    Default: false
  ;   :icon (keyword)(opt)
  ;   :icon-family (keyword)(opt)
  ;    :material-icons-filled, :material-icons-outlined
  ;    Default: :material-icons-filled
  ;    Only w/ {:icon ...}
  ;   :indent (keyword)(opt)
  ;    :left, :right, :both, :none
  ;    Default: :none
  ;   :label (metamorphic-content)
  ;   :layout (keyword)(opt)
  ;    :fit, :row
  ;    Default: :row
  ;   :on-click (metamorphic-event)(constant)(opt)
  ;    TODO ... A chip elem egésze kattintható
  ;   :on-delete (metamorphic-event)(constant)(opt)
  ;   :style (map)(opt)}
  ;
  ; @usage
  ;  [elements/chip {...}]
  ;
  ; @usage
  ;  [elements/chip :my-chip {...}]
  ([chip-props]
   [element (a/id) chip-props])

  ([chip-id chip-props]
   (let [chip-props (chip-props-prototype chip-props)]
        [chip chip-id chip-props])))
