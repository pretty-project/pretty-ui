
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript web application framework
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns elements.horizontal-polarity.views
    (:require [elements.horizontal-polarity.helpers    :as horizontal-polarity.helpers]
              [elements.horizontal-polarity.prototypes :as horizontal-polarity.prototypes]
              [random.api                              :as random]
              [x.components.api                        :as x.components]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- start-content
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) polarity-id
  ; @param (map) polarity-props
  ;  {:start-content (metamorphic-content)}
  [_ {:keys [start-content]}]
  (if start-content [:div.e-horizontal-polarity--start-content [x.components/content start-content]]
                    [:div.e-horizontal-polarity--placeholder]))

(defn- middle-content
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) polarity-id
  ; @param (map) polarity-props
  ;  {:middle-content (metamorphic-content)}
  [_ {:keys [middle-content]}]
  (if middle-content [:div.e-horizontal-polarity--middle-content [x.components/content middle-content]]
                     [:div.e-horizontal-polarity--placeholder]))

(defn- end-content
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) polarity-id
  ; @param (map) polarity-props
  ;  {:end-content (metamorphic-content)}
  [_ {:keys [end-content orientation]}]
  (if end-content [:div.e-horizontal-polarity--end-content [x.components/content end-content]]
                  [:div.e-horizontal-polarity--placeholder]))

(defn- horizontal-polarity
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) polarity-id
  ; @param (map) polarity-props
  [polarity-id polarity-props]
  [:div.e-horizontal-polarity (horizontal-polarity.helpers/polarity-attributes polarity-id polarity-props)
                              [start-content                                   polarity-id polarity-props]
                              [middle-content                                  polarity-id polarity-props]
                              [end-content                                     polarity-id polarity-props]])

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
  ;  [horizontal-polarity {...}]
  ;
  ; @usage
  ;  [horizontal-polarity :my-horizontal-polarity {...}]
  ;
  ; @usage
  ;  [horizontal-polarity {:start-content [:<> [label {:content "My label"}]
  ;                                            [label {:content "My label"}]]}]
  ([polarity-props]
   [element (random/generate-keyword) polarity-props])

  ([polarity-id polarity-props]
   (let [];polarity-props (horizontal-polarity.prototypes/polarity-props-prototype polarity-props)
        [horizontal-polarity polarity-id polarity-props])))
