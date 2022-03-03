
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-elements.components.blank
    (:require [mid-fruits.candy          :refer [param]]
              [x.app-components.api      :as components]
              [x.app-core.api            :as a :refer [r]]
              [x.app-elements.engine.api :as engine]))



;; -- Prototypes --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- blank-props-prototype
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) blank-props
  ;
  ; @return (map)
  [blank-props]
  (merge {}
         (param blank-props)))



;; -- Components --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- blank
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) blank-id
  ; @param (map) blank-props
  ;  {:content (metamorphic-content)}
  [blank-id {:keys [content] :as blank-props}]
  [:div.x-blank (engine/element-attributes blank-id blank-props)
                [components/content        blank-id content]
                [engine/element-stickers   blank-id blank-props]])

(defn element
  ; @param (keyword)(opt) blank-id
  ; @param (map) blank-props
  ;  {:class (keyword or keywords in vector)(opt)
  ;   :content (metamorphic-content)(opt)
  ;   :stickers (maps in vector)(opt)
  ;    [{:disabled? (boolean)(opt)
  ;       Default: false
  ;      :icon (keyword)
  ;      :icon-family (keyword)(opt)
  ;       :material-icons-filled, :material-icons-outlined
  ;       Default: :material-icons-filled
  ;      :on-click (metamorphic-event)(opt)
  ;      :tooltip (metamorphic-content)(opt)}]
  ;   :style (map)(opt)}
  ;
  ; @usage
  ;  [elements/blank {:content "My text"}]
  ;
  ; @usage
  ;  [elements/blank {:content :my-dictionary-term-id}]
  ;
  ; @usage
  ;  (defn my-component [_ _])
  ;  [elements/blank {:content #'my-component}]
  ([blank-props]
   [element (a/id) blank-props])

  ([blank-id blank-props]
   (let [];blank-props (blank-props-prototype blank-props)
        [blank blank-id blank-props])))
