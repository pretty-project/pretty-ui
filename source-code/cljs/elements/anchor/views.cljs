
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns elements.anchor.views
    (:require [elements.anchor.helpers    :as anchor.helpers]
              [elements.anchor.prototypes :as anchor.prototypes]
              [random.api                 :as random]
              [x.components.api           :as x.components]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- anchor-body
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) anchor-id
  ; @param (map) anchor-props
  ;  {:content (metamorphic-content)}
  [anchor-id {:keys [content] :as anchor-props}]
  [:a.e-anchor--body (anchor.helpers/anchor-body-attributes anchor-id anchor-props)
                     [x.components/content content]])

(defn- anchor
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) anchor-id
  ; @param (map) anchor-props
  [anchor-id anchor-props]
  ; Az anchor elemet azért szükséges felosztani anchor és anchor-body komponensekre,
  ; hogy a disabled állapotot megfelelően lehessen alkalmazni.
  ; Az elemet disabled állapotában eltakaró overlay az elem kattintható komponensének
  ; vagy a kattintható komponens valamely ősének szomszédos eleme kell legyen.
  [:div.e-anchor (anchor.helpers/anchor-attributes anchor-id anchor-props)
                 [anchor-body                      anchor-id anchor-props]])

(defn element
  ; XXX#9085
  ; Az anchor elem {:on-click [:x.router/go-to! "..."]} paraméterezés helyett
  ; {:href "..."} paraméterezéssel való használata lehetővé teszi az útvonal új lapon
  ; történő megnyitását.
  ;
  ; @param (keyword)(opt) anchor-id
  ; @param (map) anchor-props
  ;  {:color (keyword or string)(opt)
  ;    :default, :muted, :primary, :secondary, :success, :warning
  ;    Default: :primary
  ;   :class (keyword or keywords in vector)(opt)
  ;   :content (metamorphic-content)
  ;   :disabled? (boolean)(opt)
  ;    Default: false
  ;   :font-size (keyword)(opt)
  ;    :xxs, :xs, :s, :m, :l, :xl, :xxl, :inherit
  ;    Default: :s
  ;   :href (string)(opt)
  ;   :indent (map)(opt)
  ;    {:bottom (keyword)(opt)
  ;      :xxs, :xs, :s, :m, :l, :xl, :xxl
  ;     :left (keyword)(opt)
  ;      :xxs, :xs, :s, :m, :l, :xl, :xxl
  ;     :right (keyword)(opt)
  ;      :xxs, :xs, :s, :m, :l, :xl, :xxl
  ;     :top (keyword)(opt)
  ;      :xxs, :xs, :s, :m, :l, :xl, :xxl}
  ;   :line-height (keyword)(opt)
  ;    :block, :normal
  ;    Default: :normal
  ;   :on-click (metamorphic-event)(opt)
  ;   :stop-propagation? (boolean)(opt)
  ;    Default: false
  ;   :style (map)(opt)}
  ;
  ; @usage
  ;  [anchor {...}]
  ;
  ; @usage
  ;  [anchor :my-anchor {...}]
  ([anchor-props]
   [element (random/generate-keyword) anchor-props])

  ([anchor-id anchor-props]
   (let [anchor-props (anchor.prototypes/anchor-props-prototype anchor-props)]
        [anchor anchor-id anchor-props])))
