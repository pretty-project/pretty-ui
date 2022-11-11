
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns elements.combo-box.views
    (:require [elements.combo-box.helpers    :as combo-box.helpers]
              [elements.combo-box.prototypes :as combo-box.prototypes]
              [elements.text-field.helpers   :as text-field.helpers]
              [elements.text-field.views     :as text-field.views]
              [loop.api                      :refer [reduce-indexed]]
              [mid-fruits.random             :as random]
              [re-frame.api                  :as r]
              [reagent.api                   :as reagent]
              [x.app-components.api          :as x.components]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- default-option-component
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) box-id
  ; @param (map) box-props
  ;  {:option-label-f (function)}
  ; @param (map) option
  [_ {:keys [option-label-f]} option]
  [:div.e-combo-box--option-label (-> option option-label-f x.components/content)])

(defn- combo-box-option
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) box-id
  ; @param (map) box-props
  ;  {:option-component (component)(opt)}
  ; @param (integer) option-dex
  ; @param (map) option
  [box-id {:keys [option-component] :as box-props} option-dex option]
  ; BUG#2105
  ;  A combo-box elemhez tartozó surface felületen történő on-mouse-down esemény
  ;  a mező on-blur eseményének triggerelésével jár, ami a surface felület
  ;  React-fából történő lecsatolását okozná.
  [:button.e-combo-box--option {:on-mouse-down #(do (.preventDefault %))
                                :on-mouse-up   #(do (r/dispatch [:elements.combo-box/select-option! box-id box-props option]))
                               ;:data-selected ...
                                :data-highlighted (= option-dex (combo-box.helpers/get-highlighted-option-dex box-id))}
                               (if option-component [option-component         box-id box-props option]
                                                    [default-option-component box-id box-props option])])

(defn- combo-box-field-content-option
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) box-id
  ; @param (map) box-props
  [box-id box-props]
  ; HACK#1450
  ; Amikor a text-field elem input mezője fókuszált állapotban van, akkor a mező alatt
  ; megjelenő surface felület minden esetben {:visible? true} állapotba lép,
  ; még akkor is, amikor az opciók listája nem tartalmazna elemet.
  ; A {:visible? true} állapotban lévő felület, ha nem jelenít meg sem választható
  ; opciót, sem pedig a options-placeholder feliratot (ki lett kapcsolva), akkor
  ; a felhasználó számára nem lenne látható, miközben az állapota {:visible? true}.
  ; Ilyen esetben előfordulhatna, ha az [:elements.combo-box/ESC-pressed ...]
  ; esemény, mivel azt érzékelné, hogy a felület {:visible? true} állapotban van,
  ; ezért az ESC billentyű első lenyomására a felületet {:visible? false} állapotba
  ; állítaná, majd csak a második lenyomásra ürítené ki a mezőt, de mivel a felület
  ; a felhasználó számára nem lenne látható, ezért úgy tűnne, mintha a mező kiürítéséhez
  ; kettőször kellene megnyomni az ESC billentyűt.
  ; Ezért jelenik meg a combo-box-field-content-option elem, hogy a {:visible? true} állapotban
  ; lévő felületen mindig legyen valami tartalom és a felhasználó számára is látható
  ; legyen, hogy mi történik az ESC billentyű első lenyomásakor, akkor is, amikor
  ; nincsenek kiválasztható opciók.
  ;
  ; Egy másik megoldás az lenne, ha az [:elements.combo-box/ESC-pressed ...] esemény
  ; nem a felület {:visible? ...} állapotát vizsgálná, hanem megnézné, hogy van-e
  ; megjelenített kiválasztható opció tehát a felhasználó ténylegesen látja-e
  ; a felületet és az alapján döntené el, hogy mi történjen.
  ;
  ; Egy harmadik megoldás lenne az options-placeholder felirat használata.
  ;
  ; Egy negyedik megoldás, hogy a combo-box a google.com kereső mezőjéhez hasonlóan
  ; működne és az első választható opció mindig a beírt érték lenne, a mező tartalma
  ; pedig előnézetben mutatná a highlighted opció értékét, ami csak íródna a value-path
  ; útvonalra, amikor ténylegesen ki lett választva az adott opció.
  (let [field-content (text-field.helpers/get-field-content box-id)]
       (if-not (-> field-content str empty?)
               [:div.e-combo-box--field-content-option field-content])))

(defn- combo-box-options
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) box-id
  ; @param (map) box-props
  [box-id box-props]
  (let [options (combo-box.helpers/get-rendered-options box-id box-props)]
       (letfn [(f [option-list option-dex option]
                  ;^{:key (random/generate-react-key)}
                  (conj option-list [combo-box-option box-id box-props option-dex option]))]
              [:div.e-combo-box--options {:data-hide-scrollbar true}
                                         [combo-box-field-content-option box-id box-props]
                                         (reduce-indexed f [:<>] options)])))

(defn- combo-box-options-placeholder
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) box-id
  ; @param (map) box-props
  ;  {:options-placeholder (metamorphic-content)}
  [box-id {:keys [options-placeholder]}]
  [:div.e-combo-box--options-placeholder ; BUG#2105
                                        {:on-mouse-down #(.preventDefault %)
                                         :on-mouse-up   #(r/dispatch [:elements.text-field/hide-surface! box-id])}
                                        (x.components/content options-placeholder)])

(defn- combo-box-surface
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) box-id
  ; @param (map) box-props
  [box-id box-props]
  [:div.e-combo-box--surface [combo-box-options box-id box-props]])
                             ; Szükségtelen megjeleníteni az options-placeholder feliratot.
                             ; HACK#1450
                             ; [combo-box-options-placeholder box-id box-props]

(defn- combo-box-structure
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) box-id
  ; @param (map) box-props
  [box-id box-props]
  (let [box-props (assoc box-props :surface [combo-box-surface box-id box-props])]
       [text-field.views/element box-id box-props]))

(defn- combo-box
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) box-id
  ; @param (map) box-props
  [box-id box-props]
  (reagent/lifecycles {:component-did-mount (combo-box.helpers/component-did-mount-f box-id box-props)
                       :reagent-render      (fn [_ box-props] [combo-box-structure box-id box-props])}))

(defn element
  ; XXX#0714 (source-code/cljs/elements/text_field/views.cljs)
  ; A combo-box elem alapkomponense a text-field elem.
  ; A combo-box elem további paraméterezését a text-field elem dokumentációjában találod.
  ;
  ; @param (keyword)(opt) box-id
  ; @param (map) box-props
  ;  {:field-content-f (function)(opt)
  ;    Default: return
  ;   :field-value-f (function)(opt)
  ;    Default: return
  ;   :initial-options (vector)(opt)
  ;   :on-select (metamorphic-event)(opt)
  ;   :option-component (component)(opt)
  ;    Default: elements.combo-box.views/default-option-component
  ;   :option-label-f (function)(opt)
  ;    Default: return
  ;   :option-value-f (function)(opt)
  ;    Default: return
  ;   :options (vector)(opt)
  ;   :options-path (vector)(opt)
  ;   :options-placeholder (metamorphic-content)(opt)
  ;    Default: :no-options}
  ;
  ; @usage
  ;  [combo-box {...}]
  ;
  ; @usage
  ;  [combo-box :my-combo-box {...}]
  ([box-props]
   [element (random/generate-keyword) box-props])

  ([box-id box-props]
   (let [box-props (combo-box.prototypes/box-props-prototype  box-id box-props)]
        ; WARNING! DEPRECATED! DO NOT USE!
        ;box-props (combo-box.prototypes/box-events-prototype box-id box-props)
        [combo-box box-id box-props])))
