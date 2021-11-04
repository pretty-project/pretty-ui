
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2020.10.07
; Description:
; Version: v3.0.2
; Compatibility: x4.2.6



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-ui.sidebar
    (:require [mid-fruits.candy      :refer [param return]]
              [x.app-components.api  :as components]
              [x.app-core.api        :as a :refer [r]]
              [x.app-db.api          :as db]
              [x.app-elements.api    :as elements]
              [x.app-environment.api :as environment]))



;; -- Configuration -----------------------------------------------------------
;; ----------------------------------------------------------------------------

; @constant (ms)
(def ANIMATION-DURATION 350)

; @constant (ms)
;  BUG#0845
;  A sidebar React-fába csatolása után késleltetve jelenik meg, ellenkező esetben
;  a sidebar-megjelenítő animáció véletlenszerűen nem játszódna le.
(def SHOW-SIDEBAR-DELAY 50)



;; -- Subscriptions -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn sidebar-visible?
  ; @return (boolean)
  [db _]
  (get-in db (db/meta-item-path ::primary :sidebar-visible?)))

(defn sidebar-hidden?
  ; @return (boolean)
  [db _]
  (not (r sidebar-visible? db)))

(defn get-sidebar-hiding-duration
  ; @return (ms)
  [db _]
  (if (r sidebar-hidden? db)
      (return 0)
      (return ANIMATION-DURATION)))

(defn- get-view-props
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @return (map)
  [db _]
  (merge (get-in db (db/meta-item-path ::primary))
         {:touch-detected? (r environment/touch-events-api-detected? db)}))

(a/reg-sub ::get-view-props get-view-props)



;; -- Effect events -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :x.app-ui/empty-sidebar!
  ; @usage
  ;  [:x.app-ui/empty-sidebar!]
  {:dispatch-later [{:ms                  0 :dispatch [:x.app-ui/hide-sidebar!]}
                    {:ms ANIMATION-DURATION :dispatch [:x.app-db/remove-partition! ::primary]}]})

(a/reg-event-fx
  :x.app-ui/set-sidebar!
  ; @param (keyword)(opt) content-id
  ; @param (map) content-props
  ;  {:content (metamorphic-content)(opt)
  ;   :content-props (map)(opt)
  ;   :subscriber (subscription vector)(opt)}
  ;
  ; @usage
  ;  [:x.app-ui/set-sidebar! {...}]
  ;
  ; @usage
  ;  [:x.app-ui/set-sidebar! :my-sidebar {...}]
  (fn [_ event-vector]
      (let [content-id    (a/event-vector->second-id   event-vector)
            content-props (a/event-vector->first-props event-vector)]
           {:dispatch [:x.app-db/set-item! (db/meta-item-path ::primary :sidebar-surface)
                                           (param content-props)]
            ; BUG#0845
            :dispatch-later [{:ms SHOW-SIDEBAR-DELAY :dispatch [:x.app-ui/show-sidebar!]}]})))

(a/reg-event-fx
  :x.app-ui/hide-sidebar!
  ; @usage
  ;  [:x.app-ui/hide-sidebar!]
  (fn [{:keys [db]} _]
      (if (r sidebar-visible? db)
          {:dispatch-n [[:x.app-environment.scroll-prohibitor/remove-scroll-prohibition! :app-sidebar]
                        [:x.app-db/set-item! (db/meta-item-path ::primary :sidebar-visible?)
                                             (param false)]]})))

(a/reg-event-fx
  :x.app-ui/show-sidebar!
  ; @usage
  ;  [:x.app-ui/show-sidebar!]
  (fn [{:keys [db]} _]
      (if (r sidebar-hidden? db)
          {:dispatch-n [[:x.app-environment.scroll-prohibitor/add-scroll-prohibition! :app-sidebar]
                        [:x.app-db/set-item! (db/meta-item-path ::primary :sidebar-visible?)
                                             (param true)]]})))



;; -- Components --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- sidebar-controls
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) sidebar-id
  ; @param (map) view-props
  ;
  ; @return (hiccup)
  [_ _]
  [:div#x-app-sidebar--controls
    [elements/button {:color   :none
                      :icon    :close
                      :layout  :icon-button
                      :variant :transparent
                      :on-click
                      [:x.app-ui/hide-sidebar!]}]])

(defn- sidebar-surface
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) sidebar-id
  ; @param (map) view-props
  ;  {:sidebar-surface (map)
  ;   :touch-detected? (boolean)}
  ;
  ; @return (hiccup)
  [_ {:keys [sidebar-surface touch-detected?]}]
  [:div#x-app-sidebar--surface
    [components/content sidebar-surface]

    ; XXX#1980
    ; Mobil eszközökön a képernyő alsó részén látható böngésző eszközsáv
    ; eltakarná a sidebar tartalmának alsó részét.
    (if touch-detected? [:div#x-app-sidebar--placeholder])])

(defn- sidebar-cover
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) sidebar-id
  ; @param (map) view-props
  ;  {:sidebar-visible? (boolean)}
  ;
  ; @return (hiccup)
  [_ {:keys [sidebar-visible?]}]
  [:div#x-app-sidebar--cover {:data-visible sidebar-visible?
                              :on-click #(a/dispatch [:x.app-ui/hide-sidebar!])}])

(defn- sidebar-body
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) sidebar-id
  ; @param (map) view-props
  ;
  ; @return (hiccup)
  [sidebar-id view-props]
  [:div#x-app-sidebar--body
    [sidebar-surface  sidebar-id view-props]
    [sidebar-controls sidebar-id view-props]])

(defn- sidebar
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) sidebar-id
  ; @param (map) view-props
  ;  {:sidebar-surface (map)
  ;   :sidebar-visible? (boolean)}
  ;
  ; @return (component)
  [sidebar-id {:keys [sidebar-surface sidebar-visible?] :as view-props}]
  (if (some? sidebar-surface)
      [:<> [sidebar-cover sidebar-id view-props]
           [:div#x-app-sidebar {:data-nosnippet true
                                :data-visible sidebar-visible?}
             [sidebar-body sidebar-id view-props]]]))

(defn view
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @return (component)
  []
  [components/subscriber ::view
                         {:component  #'sidebar
                          :subscriber [::get-view-props]}])
