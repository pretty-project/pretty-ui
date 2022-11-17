
; WARNING
; Az x.ui.renderer.views névtér újraírása szükséges, a Re-Frame adatbázis
; események számának csökkentése érdekében!



;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.ui.renderer.views
    (:require [hiccup.api               :as hiccup]
              [re-frame.api             :as r :refer [r]]
              [reagent.api              :as reagent]
              [vector.api               :as vector]
              [x.ui.renderer.helpers    :as renderer.helpers]
              [x.ui.renderer.prototypes :as renderer.prototypes]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- wrapper
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) renderer-id
  ; @param (map) renderer-props
  ;  {:attributes (map)(opt)}
  [renderer-id {:keys [attributes]}]
  (let [dom-id (renderer.helpers/renderer-id->dom-id renderer-id)
        wrapper-attributes (assoc attributes :id (hiccup/value dom-id))]
       [:div wrapper-attributes]))

(defn- elements
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) renderer-id
  ; @param (map) renderer-props
  ;  {:element (component)}
  [renderer-id {:keys [element] :as renderer-props}]
  (letfn [(f [wrapper element-id]
             (conj wrapper ^{:key element-id} [element element-id]))]
         (let [element-order @(r/subscribe [:x.ui/get-element-order renderer-id])]
              (reduce f (wrapper renderer-id renderer-props) element-order))))

(defn renderer
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) renderer-id
  ; @param (map) renderer-props
  [renderer-id renderer-props]
  ; BUG#0009
  ; Ha egy renderer nem jelenít meg elemeket, akkor a wrapper komponense nincs
  ; a React-fába csatolva.
  ; Ennek következtében a {flex-direction: row-reverse} beállítással használt
  ; [:div#x-app-ui-structure] elemben addig a pillanatig, amíg nem jelenik meg
  ; az [:div#x-app-surface] elem, addig az [:div#x-app-sidebar] elem a viewport
  ; jobb oldalán jelenik meg.
  ; Ennek elkerülése érdekében a wrapper komponensek mindenképpen megjelennek,
  ; így nem fordulhat elő olyan pillanat az applikáció betöltése közben, hogy
  ; a sidebar a surface hiánya miatt a jobb oldalon jelenne meg.
  (let [element-order @(r/subscribe [:x.ui/get-element-order renderer-id])]
       (if (vector/nonempty? element-order)
           [elements renderer-id renderer-props]
           [wrapper  renderer-id renderer-props])))

(defn component
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) renderer-id
  ; @param (map) renderer-props
  ;  {:alternate-renderer (keyword)(opt)
  ;    W/ {:required? true}
  ;   :element (component)
  ;   :max-elements-rendered (integer)(opt)
  ;    Default: DEFAULT-MAX-ELEMENTS-RENDERED
  ;   :queue-behavior (keyword)(opt)
  ;    :ignore, :push, :wait
  ;    Default: :wait
  ;   :required? (boolean)(opt)
  ;    Default: false
  ;   :rerender-same? (boolean)(opt)
  ;    Default: false}
  [renderer-id renderer-props]
  (let [renderer-props (renderer.prototypes/renderer-props-prototype renderer-props)]
       (reagent/lifecycles (renderer.helpers/renderer-id->dom-id renderer-id)
                           {:reagent-render         (fn []             [renderer                 renderer-id renderer-props])
                            :component-will-unmount (fn [] (r/dispatch [:x.ui/destruct-renderer! renderer-id renderer-props]))
                            :component-did-mount    (fn [] (r/dispatch [:x.ui/init-renderer!     renderer-id renderer-props]))})))
