
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-elements.element-components.vertical-polarity
    (:require [mid-fruits.candy          :refer [param]]
              [mid-fruits.random         :as random]
              [x.app-components.api      :as x.components]
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
  [_ {:keys [start-content]}]
  (if start-content [:div.x-vertical-polarity--start-content [x.components/content start-content]]
                    [:div.x-vertical-polarity--placeholder]))

(defn- middle-content
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) polarity-id
  ; @param (map) polarity-props
  ;  {:middle-content (metamorphic-content)}
  [_ {:keys [middle-content]}]
  (if middle-content [:div.x-vertical-polarity--middle-content [x.components/content middle-content]]
                     [:div.x-vertical-polarity--placeholder]))

(defn- end-content
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) polarity-id
  ; @param (map) polarity-props
  ;  {:end-content (metamorphic-content)}
  [_ {:keys [end-content orientation]}]
  (if end-content [:div.x-vertical-polarity--end-content [x.components/content end-content]]
                  [:div.x-vertical-polarity--placeholder]))

(defn- vertical-polarity
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) polarity-id
  ; @param (map) polarity-props
  [polarity-id polarity-props]
  [:div.x-vertical-polarity (engine/element-attributes polarity-id polarity-props)
                            [start-content             polarity-id polarity-props]
                            [middle-content            polarity-id polarity-props]
                            [end-content               polarity-id polarity-props]])

(defn element
  ; @param (keyword)(opt) polarity-id
  ; @param (map) polarity-props
  ;  {:class (keyword or keywords in vector)(opt)
  ;   :end-content (metamorphic-content)
  ;   :indent (map)(opt)
  ;    {:bottom (keyword)(opt)
  ;      :xxs, :xs, :s, :m, :l, :xl, :xxl
  ;     :left (keyword)(opt)
  ;      :xxs, :xs, :s, :m, :l, :xl, :xxl
  ;     :right (keyword)(opt)
  ;      :xxs, :xs, :s, :m, :l, :xl, :xxl
  ;     :top (keyword)(opt)
  ;      :xxs, :xs, :s, :m, :l, :xl, :xxl}
  ;   :middle-content (metamorphic-content)
  ;   :style (map)(opt)
  ;   :start-content (metamorphic-content)(opt)
  ;
  ; @usage
  ;  [vertical-polarity {...}]
  ;
  ; @usage
  ;  [vertical-polarity :my-vertical-polarity {...}]
  ;
  ; @usage
  ;  [vertical-polarity {:start-content [:<> [label {:content "My label"}]
  ;                                          [label {:content "My label"}]]}]
  ([polarity-props]
   [element (random/generate-keyword) polarity-props])

  ([polarity-id polarity-props]
   (let [];polarity-props (polarity-props-prototype polarity-props)
        [vertical-polarity polarity-id polarity-props])))
