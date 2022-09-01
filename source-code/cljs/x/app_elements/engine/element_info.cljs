
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-elements.engine.element-info
    (:require [mid-fruits.candy      :refer [param]]
              [x.app-components.api  :as components]
              [x.app-core.api        :as a]
              [x.app-environment.api :as environment]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn info-text-visible?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) element-id
  ;
  ; @return (boolean)
  [db [_ element-id]]
  (get-in db [:elements :element-handler/meta-items element-id :info-text-visible?]))

(a/reg-sub :elements/info-text-visible? info-text-visible?)



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn toggle-info-text!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) element-id
  ;
  ; @return (map)
  [db [_ element-id]]
  (update-in db [:elements :element-handler/meta-items element-id :info-text-visible?] not))

(a/reg-event-db :elements/toggle-info-text! toggle-info-text!)



;; -- Components --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- element-helper
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) element-id
  ; @param (map) element-props
  ;  {:helper (metamorphic-content)}
  [_ {:keys [helper]}]
  (if helper [:div.x-element--helper (components/content helper)]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn info-text-content
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) element-id
  ; @param (map) element-props
  ;  {:info-text (metamorphic-content)(opt)}
  [element-id {:keys [info-text]}]
  (if info-text (if-let [info-text-visible? @(a/subscribe [:elements/info-text-visible? element-id])]
                        [:div.x-element--info-text--content (components/content info-text)])))

(defn info-text-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) element-id
  ; @param (map) element-props
  ;  {:info-text (metamorphic-content)(opt)}
  [element-id {:keys [info-text]}]
  (if info-text [:button.x-element--info-text--button {:data-icon-family :material-icons-filled
                                                       :on-click    #(a/dispatch [:elements/toggle-info-text! element-id])
                                                       :on-mouse-up #(environment/blur-element!)}
                                                      (param :info_outline)]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn element-header
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) element-id
  ; @param (map) element-props
  ;  {}
  [element-id {:keys [info-text label required?] :as element-props}]
  ; Az element-header komponens jeleníti meg az egyes elemek címkéjét, a {:required? true}
  ; állapotjelzőt és az info-text-button gombot.
  ;
  ; Ha az elem {:required? ...} tulajdonságának értéke :unmarked, akkor az elem
  ; {:required? true} állapotban van, tehát többek közt az engine/input-passed?
  ; függvény kötelező inputnak tekinti, de közben a {:required? true} állapotot jelölő
  ; piros csillag és az input kitöltésésére figyelmeztető piros szöveg nem jelenik meg.
  ;
  ; Pl.: A bejelentkező képernyőn lévő email-address és password mezők {:required? true}
  ;      állapotban kell, hogy legyenek, hogy a login submit-button {:disabled? true}
  ;      állapotban lehessen mindaddig, amíg a mezők nincsenek kitöltve, miközben
  ;      a mezőkön nem jelennek meg {:required? true} állapotra utaló jelölések.
  [:<> (if label [:div.x-element--header [:div.x-element--label (components/content label)
                                                                (if (true? required?)
                                                                    [:span.x-element--label-asterisk "*"])]
                                    [info-text-button element-id element-props]])
       [element-helper    element-id element-props]
       [info-text-content element-id element-props]])
