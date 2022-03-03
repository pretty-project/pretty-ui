
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-elements.components.horizontal-polarity
    (:require [mid-fruits.candy          :refer [param]]
              [x.app-components.api      :as components]
              [x.app-core.api            :as a]
              [x.app-elements.engine.api :as engine]))



;; -- Prototypes --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- polarity-props-prototype
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) polarity-props
  ;
  ; @return (map)
  [polarity-props]
  (merge {}
         (param polarity-props)))



;; -- Components --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- start-content
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) polarity-id
  ; @param (map) polarity-props
  ;  {:start-content (metamorphic-content)}
  ;
  ; @return (component)
  [_ {:keys [start-content]}]
  (if start-content [:div.x-horizontal-polarity--start-content [components/content start-content]]
                    [:div.x-horizontal-polarity--placeholder]))

(defn- middle-content
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) polarity-id
  ; @param (map) polarity-props
  ;  {:middle-content (metamorphic-content)}
  ;
  ; @return (component)
  [_ {:keys [middle-content]}]
  (if middle-content [:div.x-horizontal-polarity--middle-content [components/content middle-content]]
                     [:div.x-horizontal-polarity--placeholder]))

(defn- end-content
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) polarity-id
  ; @param (map) polarity-props
  ;  {:end-content (metamorphic-content)}
  ;
  ; @return (component)
  [_ {:keys [end-content orientation]}]
  (if end-content [:div.x-horizontal-polarity--end-content [components/content end-content]]
                  [:div.x-horizontal-polarity--placeholder]))

(defn- horizontal-polarity
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) polarity-id
  ; @param (map) polarity-props
  ;
  ; @return (hiccup)
  [polarity-id polarity-props]
  [:div.x-horizontal-polarity (engine/element-attributes polarity-id polarity-props)
                              [start-content             polarity-id polarity-props]
                              [middle-content            polarity-id polarity-props]
                              [end-content               polarity-id polarity-props]])

(defn element
  ; @param (keyword)(opt) polarity-id
  ; @param (map) polarity-props
  ;  {:class (keyword or keywords in vector)(opt)
  ;   :end-content (metamorphic-content)
  ;   :indent (keyword)(opt)
  ;    :left, :right, :both, :none
  ;    Default: :none
  ;   :middle-content (metamorphic-content)
  ;   :style (map)(opt)
  ;   :start-content (metamorphic-content)(opt)
  ;
  ; @usage
  ;  [elements/horizontal-polarity {...}]
  ;
  ; @usage
  ;  [elements/horizontal-polarity :my-horizontal-polarity {...}]
  ;
  ; @usage
  ;  [elements/horizontal-polarity {:start-content [:<> [elements/label {:content "My label"}]
  ;                                                     [elements/label {:content "My label"}]]}]
  ;
  ; @return (component)
  ([polarity-props]
   [element (a/id) polarity-props])

  ([polarity-id polarity-props]
   (let [];polarity-props (polarity-props-prototype polarity-props)
        [horizontal-polarity polarity-id polarity-props])))
