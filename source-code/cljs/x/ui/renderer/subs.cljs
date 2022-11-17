
; WARNING
; Az x.ui.renderer.subs névtér újraírása szükséges, a Re-Frame adatbázis
; események számának csökkentése érdekében!



;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.ui.renderer.subs
    (:require [candy.api             :refer [return]]
              [re-frame.api          :as r :refer [r]]
              [vector.api            :as vector]
              [x.ui.renderer.config  :as renderer.config]
              [x.ui.renderer.helpers :as renderer.helpers]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-element-order
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) renderer-id
  ;
  ; @return (keywords in vector)
  [db [_ renderer-id]]
  (let [data-order-key (renderer.helpers/data-key renderer-id :data-order)]
       (get-in db [:x.ui data-order-key])))

(defn get-rendered-element-order
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) renderer-id
  ;
  ; @return (vector)
  [db [_ renderer-id]]
  (let [data-order-key (renderer.helpers/data-key renderer-id :data-order)]
       (get-in db [:x.ui data-order-key])))

(defn get-invisible-element-ids
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) renderer-id
  ;
  ; @return (vector)
  [db [_ renderer-id]]
  (let [meta-items-key (renderer.helpers/data-key renderer-id :meta-items)]
       (get-in db [:x.ui meta-items-key :invisible-elements])))

(defn get-visible-element-order
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) renderer-id
  ;
  ; @return (vector)
  [db [_ renderer-id]]
  ; WARNING!
  ; A visible-element order vektor külön kell majd tárolni!
  ; Ne a feliratkozások számítsák ki az értékét!
  (let [rendered-element-order (r get-rendered-element-order db renderer-id)
        invisible-element-ids  (r get-invisible-element-ids  db renderer-id)]
       (vector/remove-items rendered-element-order invisible-element-ids)))

(defn any-element-rendered?
  ; @param (keyword) renderer-id
  ;
  ; @return (boolean)
  [db [_ renderer-id]]
  (let [rendered-element-order (r get-rendered-element-order db renderer-id)]
       (vector/nonempty? rendered-element-order)))

(defn any-element-invisible?
  ; @param (keyword) renderer-id
  ;
  ; @return (boolean)
  [db [_ renderer-id]]
  (let [invisible-element-ids (r get-invisible-element-ids db renderer-id)]
       (vector/nonempty? invisible-element-ids)))

(defn any-element-visible?
  ; @param (keyword) renderer-id
  ;
  ; @return (boolean)
  [db [_ renderer-id]]
  (let [visible-element-order (r get-visible-element-order db renderer-id)]
       (vector/nonempty? visible-element-order)))

(defn no-visible-elements?
  ; @param (keyword) renderer-id
  ;
  ; @return (boolean)
  [db [_ renderer-id]]
  (let [any-element-visible? (r any-element-visible? db renderer-id)]
       (not any-element-visible?)))

(defn get-lower-visible-element-id
  ; @param (keyword) renderer-id
  ;
  ; @return (keyword)
  [db [_ renderer-id]]
  (vector/first-item (r get-visible-element-order db renderer-id)))

(defn get-max-elements-rendered
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) renderer-id
  ;
  ; @return (integer)
  [db [_ renderer-id]]
  (let [meta-items-key (renderer.helpers/data-key renderer-id :meta-items)]
       (get-in db [:x.ui meta-items-key :max-elements-rendered])))

(defn max-elements-reached?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) renderer-id
  ;
  ; @return (boolean)
  [db [_ renderer-id]]
  (let [max-elements-rendered (r get-max-elements-rendered db renderer-id)
        visible-element-order (r get-visible-element-order db renderer-id)]
       (vector/count? visible-element-order max-elements-rendered)))

(defn get-renderer-queue-behavior
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) renderer-id
  ;
  ; @return (keyword)
  [db [_ renderer-id]]
  (let [meta-items-key (renderer.helpers/data-key renderer-id :meta-items)]
       (get-in db [:x.ui meta-items-key :queue-behavior])))

(defn pushed-rendering-enabled?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) renderer-id
  ;
  ; @return (boolean)
  [db [_ renderer-id]]
  (let [queue-behavior (r get-renderer-queue-behavior db renderer-id)]
       (= queue-behavior :push)))

(defn ignore-rendering?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) renderer-id
  ;
  ; @return (boolean)
  [db [_ renderer-id]]
  (let [queue-behavior (r get-renderer-queue-behavior db renderer-id)]
       (and (= queue-behavior :ignore)
            (r max-elements-reached? db renderer-id))))

(defn renderer-required?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) renderer-id
  ;
  ; @return (boolean)
  [db [_ renderer-id]]
  (let [meta-items-key (renderer.helpers/data-key renderer-id :meta-items)]
       (get-in db [:x.ui meta-items-key :required?])))

(defn get-alternate-renderer
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) renderer-id
  ;
  ; @return (keyword)
  [db [_ renderer-id]]
  (let [meta-items-key (renderer.helpers/data-key renderer-id :meta-items)]
       (get-in db [:x.ui meta-items-key :alternate-renderer])))

(defn renderer-require-error?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) renderer-id
  ;
  ; @return (boolean)
  [db [_ renderer-id]]
  (if-let [alternate-renderer (r get-alternate-renderer db renderer-id)]
          (and (r renderer-required?   db renderer-id)
               (r no-visible-elements? db alternate-renderer))
          (r renderer-required? db renderer-id)))

(defn renderer-reserved?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) renderer-id
  ;
  ; @return (boolean)
  [db [_ renderer-id]]
  (let [meta-items-key (renderer.helpers/data-key renderer-id :meta-items)]
       (get-in db [:x.ui meta-items-key :reserved?])))

(defn renderer-free?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) renderer-id
  ;
  ; @return (boolean)
  [db [_ renderer-id]]
  (let [meta-items-key (renderer.helpers/data-key renderer-id :meta-items)]
       (not (get-in db [:x.ui meta-items-key :reserved?]))))

(defn get-element-props
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) renderer-id
  ; @param (keyword) element-id
  ;
  ; @return (map)
  [db [_ renderer-id element-id]]
  (let [data-items-key (renderer.helpers/data-key renderer-id :data-items)]
       (get-in db [:x.ui data-items-key element-id])))

(defn get-element-prop
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) renderer-id
  ; @param (keyword) element-id
  ; @param (keyword) prop-key
  ;
  ; @return (*)
  [db [_ renderer-id element-id prop-key]]
  (let [data-items-key (renderer.helpers/data-key renderer-id :data-items)]
       (get-in db [:x.ui data-items-key element-id prop-key])))

(defn reveal-element-animated?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) renderer-id
  ; @param (keyword) element-id
  ;
  ; @return (boolean)
  [db [_ renderer-id element-id]]
  (let [reveal-animated? (r get-element-prop db renderer-id element-id :reveal-animated?)]
       (boolean reveal-animated?)))

(defn hide-element-animated?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) renderer-id
  ; @param (keyword) element-id
  ;
  ; @return (boolean)
  [db [_ renderer-id element-id]]
  (let [hide-animated? (r get-element-prop db renderer-id element-id :hide-animated?)]
       (boolean hide-animated?)))

(defn element-rendered?
  ; @param (keyword) renderer-id
  ; @param (keyword) element-id
  ;
  ; @return (boolean)
  [db [_ renderer-id element-id]]
  (let [rendered-element-order (r get-rendered-element-order db renderer-id)]
       (vector/contains-item? rendered-element-order element-id)))

(defn element-invisible?
  ; @param (keyword) renderer-id
  ; @param (keyword) element-id
  ;
  ; @return (boolean)
  [db [_ renderer-id element-id]]
  (let [invisible-element-ids (r get-invisible-element-ids db renderer-id)]
       (vector/contains-item? invisible-element-ids element-id)))

(defn element-visible?
  ; @param (keyword) renderer-id
  ; @param (keyword) element-id
  ;
  ; @return (boolean)
  [db [_ renderer-id element-id]]
  (and      (r element-rendered?  db renderer-id element-id)
       (not (r element-invisible? db renderer-id element-id))))

(defn rerender-same?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) renderer-id
  ;
  ; @return (boolean)
  [db [_ renderer-id]]
  (let [meta-items-key (renderer.helpers/data-key renderer-id :meta-items)]
       (get-in db [:x.ui meta-items-key :rerender-same?])))

(defn get-rerender-delay
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) renderer-id
  ; @param (keyword) element-id
  ;
  ; @return (ms)
  [db [_ renderer-id element-id]]
  (if (r hide-element-animated? db renderer-id element-id)
      (+ renderer.config/HIDE-ANIMATION-TIMEOUT
         renderer.config/RENDER-DELAY-OFFSET)
      (return renderer.config/RENDER-DELAY-OFFSET)))

(defn get-pushing-delay
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) renderer-id
  ; @param (keyword) element-id
  ;
  ; @return (ms)
  [db [_ renderer-id]]
  (let [lower-visible-element-id (r get-lower-visible-element-id db renderer-id)]
       (if (r hide-element-animated? db renderer-id lower-visible-element-id)
           (+ renderer.config/HIDE-ANIMATION-TIMEOUT
              renderer.config/RENDER-DELAY-OFFSET)
           (return renderer.config/RENDER-DELAY-OFFSET))))

(defn get-queue-rendering-delay
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) renderer-id
  ; @param (keyword) element-id
  ;
  ; @return (ms)
  [db [_ renderer-id element-id]]
  (if (r hide-element-animated? db renderer-id element-id)
      (+ renderer.config/HIDE-ANIMATION-TIMEOUT
         renderer.config/RENDER-DELAY-OFFSET)
      (return renderer.config/RENDER-DELAY-OFFSET)))

(defn get-destroying-delay
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) renderer-id
  ; @param (keyword) element-id
  ;
  ; @return (ms)
  [db [_ renderer-id element-id]]
  (if (r hide-element-animated? db renderer-id element-id)
      (+ renderer.config/HIDE-ANIMATION-TIMEOUT
         renderer.config/DESTROY-DELAY-OFFSET)
      (return renderer.config/DESTROY-DELAY-OFFSET)))

(defn render-element-now?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) renderer-id
  ; @param (keyword) element-id
  ;
  ; @return (boolean)
  [db [_ renderer-id _]]
  (and (r renderer-free? db renderer-id)
       (or      (r pushed-rendering-enabled? db renderer-id)
           (not (r max-elements-reached?     db renderer-id)))))

(defn get-rendering-queue
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) renderer-id
  ;
  ; @return (vectors in vector)
  ;  [[(keyword) element-id
  ;    (map) element-props]
  ;   [...]]
  [db [_ renderer-id]]
  (let [meta-items-key (renderer.helpers/data-key renderer-id :meta-items)]
       (get-in db [:x.ui meta-items-key :rendering-queue])))

(defn get-next-rendering
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) renderer-id
  ;
  ; @return (vector)
  ;  [(keyword) element-id
  ;   (map) element-props]
  [db [_ renderer-id]]
  (let [rendering-queue (r get-rendering-queue db renderer-id)]
       (vector/first-item rendering-queue)))

(defn rerender-element?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) renderer-id
  ; @param (keyword) element-id
  ; @param (map) element-props
  ;
  ; @return (boolean)
  [db [_ renderer-id element-id _]]
  (and (r element-rendered? db renderer-id element-id)
       (r rerender-same?    db renderer-id)))

(defn update-element-animated?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) renderer-id
  ; @param (keyword) element-id
  ; @param (map) element-props
  ;  {:update-animated? (boolean)(opt)}
  ;
  ; @return (boolean)
  [db [_ renderer-id element-id {:keys [update-animated?]}]]
  (and      (r element-rendered? db renderer-id element-id)
       (not (r rerender-same?    db renderer-id))
       (boolean update-animated?)))

(defn update-element-static?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) renderer-id
  ; @param (keyword) element-id
  ; @param (map) element-props
  ;  {:update-animated? (boolean)(opt)}
  ;
  ; @return (boolean)
  [db [_ renderer-id element-id {:keys [update-animated?]}]]
  (and      (r element-rendered? db renderer-id element-id)
       (not (r rerender-same?    db renderer-id))
       (not update-animated?)))

(defn push-element?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) renderer-id
  ; @param (keyword) element-id
  ; @param (map) element-props
  ;
  ; @return (boolean)
  [db [_ renderer-id element-id _]]
  (and      (r max-elements-reached?     db renderer-id)
            (r pushed-rendering-enabled? db renderer-id)
       (not (r element-rendered?         db renderer-id element-id))))

(defn render-element-animated?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) renderer-id
  ; @param (keyword) element-id
  ; @param (map) element-props
  ;  {:reveal-animated? (boolean)(opt)}
  ;
  ; @return (boolean)
  [db [_ renderer-id element-id {:keys [reveal-animated?]}]]
  (and (not (r max-elements-reached? db renderer-id))
       (not (r element-rendered?     db renderer-id element-id))
       (boolean reveal-animated?)))

(defn render-element-static?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) renderer-id
  ; @param (keyword) element-id
  ; @param (map) element-props
  ;  {:reveal-animated? (boolean)(opt)}
  ;
  ; @return (boolean)
  [db [_ renderer-id element-id {:keys [reveal-animated?]}]]
  (and (not (r max-elements-reached? db renderer-id))
       (not (r element-rendered?     db renderer-id element-id))
       (not reveal-animated?)))

(defn destroy-element-animated?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) renderer-id
  ; @param (keyword) element-id
  ;
  ; @return (boolean)
  [db [_ renderer-id element-id]]
  (and (r element-visible?       db renderer-id element-id)
       (r hide-element-animated? db renderer-id element-id)))

(defn destroy-element-static?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) renderer-id
  ; @param (keyword) element-id
  ;
  ; @return (boolean)
  [db [_ renderer-id element-id]]
  (and      (r element-visible?       db renderer-id element-id)
       (not (r hide-element-animated? db renderer-id element-id))))

(defn get-visible-elements-destroying-event-list
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) renderer-id
  ;
  ; @return (maps in vector)
  ;  [{:ms (ms)
  ;    :dispatch (metamorphic-event)}]
  [db [_ renderer-id]]
  (let [visible-element-order (r get-visible-element-order db renderer-id)]
       (letfn [(f [event-list dex element-id]
                  ; Az időzített esemény-lista aktuális elemének eltűntetési eseményének időértéke,
                  ; az utolsó (előző) elem időértéke ({:ms ...}), összeadva az előző elem eltűnéséhez
                  ; szükséges idővel
                  (if (= dex 0)
                      ; The first destroying event in the list ...
                      [{:dispatch [:x.ui/destroy-element! renderer-id element-id] :ms 0}]
                      ; The other destroying events in the list ...
                      (let [prev-element-id  (get-in event-list [(dec dex) :dispatch 2])
                            prev-event-delay (get-in event-list [(dec dex) :ms])
                            destroying-delay (r get-destroying-delay db renderer-id prev-element-id)]
                           (conj event-list {:dispatch [:x.ui/destroy-element! renderer-id element-id]
                                             :ms       (+ prev-event-delay destroying-delay)}))))]
              (reduce-kv f [] visible-element-order))))

(defn get-visible-elements-destroying-duration
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) renderer-id
  ;
  ; @return (ms)
  [db [_ renderer-id]]
  (let [visible-element-order (r get-visible-element-order db renderer-id)]
       (letfn [(f [duration element-id]
                  (let [destroying-delay (r get-destroying-delay db renderer-id element-id)]
                       (+ duration destroying-delay)))]
              (reduce f 0 visible-element-order))))

(defn get-render-log
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) renderer-id
  ; @param (keyword) element-id
  ; @param (keyword) action-key
  ;
  ; @usage
  ;  (r renderer/get-render-log db :bubbles :my-bubble :render-requested-at)
  ;
  ; @return (ms)
  [db [_ renderer-id element-id action-key]]
  (let [meta-items-key (renderer.helpers/data-key renderer-id :meta-items)]
       (get-in db [:x.ui meta-items-key :render-log element-id action-key])))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; WARNING! NON-PUBLIC! DO NOT USE!
(r/reg-sub :x.ui/get-element-order get-element-order)

; WARNING! NON-PUBLIC! DO NOT USE!
(r/reg-sub :x.ui/get-element-props get-element-props)
