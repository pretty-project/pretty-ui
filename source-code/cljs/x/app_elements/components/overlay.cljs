
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-elements.components.overlay
    (:require [mid-fruits.candy          :refer [param]]
              [x.app-components.api      :as components]
              [x.app-core.api            :as a]
              [x.app-elements.engine.api :as engine]))



;; -- Prototypes --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- overlay-props-prototype
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) overlay-props
  ;
  ; @return (map)
  ;  {}
  [overlay-props]
  (merge {}
         (param overlay-props)))



;; -- Components --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- overlay
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) overlay-id
  ; @param (map) overlay-props
  ;  {:content (metamorphic-content)}
  ;
  ; @return (hiccup)
  [overlay-id {:keys [content] :as overlay-props}]
  [:div.x-overlay (engine/element-attributes overlay-id overlay-props)
                  [components/content overlay-id content]])

(defn element
  ; @param (keyword)(opt) overlay-id
  ; @param (map) overlay-props
  ;  {:class (keyword or keywords in vector)(opt)
  ;   :content (metamorphic-content)
  ;   :disabled? (boolean)(opt)
  ;    Default: false
  ;   :style (map)(opt)}
  ;
  ; @usage
  ;  [elements/overlay {...}]
  ;
  ; @usage
  ;  [elements/overlay :my-overlay {...}]
  ;
  ; @return (component)
  ([overlay-props]
   [element (a/id) overlay-props])

  ([overlay-id overlay-props]
   (let [];overlay-props (overlay-props-prototype overlay-props)
        [overlay overlay-id overlay-props])))
