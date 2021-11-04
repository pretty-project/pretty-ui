
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.06.16
; Description:
; Version: v1.6.0
; Compatibility: x4.4.4



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-elements.engine.input-group
    (:require [mid-fruits.candy              :refer [param return]]
              [mid-fruits.mixed              :as mixed]
              [mid-fruits.vector             :as vector]
              [x.app-core.api                :as a :refer [r]]
              [x.app-elements.engine.element :as element]
              [x.app-elements.engine.input   :as input]))



;; -- Subscriptions -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-input-group-id
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) input-id
  ;
  ; @return (keyword)
  [db [_ input-id]]
  (r element/get-element-prop db input-id :group-id))

(defn- disallow-empty-input-group?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; Ha egy input-group rendelkezik {:disallow-empty-input-group? true}
  ; tulajdonsággal, akkor az input-group értékére való feliratkozás visszatérési
  ; értéke egy legalább egy elemű vektor.
  ; Egyes elemek (pl.: multi-field) működéséhez szükséges, hogy az input-group
  ; rendelkezzen legalább egy értékkel.
  ;
  ; @param (keyword) group-id
  ;
  ; @return (boolean)
  [db [_ group-id]]
  (boolean (r element/get-element-prop db group-id :disallow-empty-input-group?)))

(defn- allow-empty-input-group?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) group-id
  ;
  ; @return (boolean)
  [db [_ group-id]]
  (not (r disallow-empty-input-group? db group-id)))

(defn- max-input-count-reached?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) group-id
  ;
  ; @return (boolean)
  [db [_ group-id]]
  (let [group-value-path (r element/get-element-prop db group-id :value-path)
        group-value      (get-in db group-value-path)
        input-count      (count group-value)
        max-input-count  (r element/get-element-prop db group-id :max-input-count)]
       (>= input-count max-input-count)))

(defn- get-input-group-value
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) group-id
  ;
  ; @return (vector)
  [db [_ group-id]]
  (if-let [group-value-path (r element/get-element-prop db group-id :value-path)]
          (let [group-value (get-in db group-value-path)]
                     ; Group-value is nonempty vector
               (cond (vector/nonempty? group-value)
                     (return           group-value)
                     ; Group-value is empty vector & allow empty input-group
                     (and (vector? group-value)
                          (r allow-empty-input-group? db group-id))
                     (return group-value)
                     ; Group-value is empty vector & disallow empty input-group
                     (vector? group-value)
                     (let [initial-value (r element/get-element-prop db group-id :initial-value)]
                          (return [initial-value]))
                     ; Group-value is not vector & allow empty input group
                     (r allow-empty-input-group? db group-id)
                     (return [])
                     ; Group-value is not vector & disallow empty input group
                     :disallow-empty-input-group?
                     (return [nil])))))

(defn get-input-group-view-props
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) group-id
  ;
  ; @return (map)
  ;  {:group-value (vector)}
  [db [_ group-id]]
  {:group-value (r get-input-group-value db group-id)})



;; -- DB events ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- conj-initial-value!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) group-id
  ; @param (map) context-props
  ;  {:initial-value (*)}
  ;
  ; @return (map)
  [db [_ group-id {:keys [initial-value]}]]
  (let [group-value-path (r element/get-element-prop db group-id :value-path)
        group-value      (get-in db group-value-path)]
             ; Group value is nonempty vector & allow empty input-group
             ; Group value is nonempty vector & disallow empty input-group
       (cond (vector/nonempty? group-value)
             (update-in db group-value-path vector/conj-item initial-value)
             ; Group value is NOT nonempty vector & disallow empty input-group
             ;
             ; Pl. ha egy multi-field group-value értéke nil, akkor EGY field
             ; jelenik meg, amely esetben a mezők számának növelése után
             ; kettő darab mezőnek kell megjelenjen, amihez szükséges, hogy group-value
             ; kettő értéket tartalmazzon.
             (r disallow-empty-input-group? db group-id)
             (assoc-in db group-value-path [initial-value initial-value])
             ; Group value is NOT nonempty vector & allow empty input-group
             (r allow-empty-input-group? db group-id)
             (assoc-in db group-value-path [initial-value]))))

(defn increase-input-count!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) group-id
  ; @param (map) context-props

  ;
  ; @example
  ;  (def db {:my-value ["First value" "Second value"]})
  ;  (r engine/increase-input-count! db :my-group {:initial-value "Apple"})
  ;  => {:my-value ["First value" "Second value" "Apple"]}
  ;
  ; @return (map)
  [db [event-id group-id context-props]]
  (let [group-value-path (r element/get-element-prop db group-id :value-path)]
       (if (r max-input-count-reached? db group-id)
           (return db)
           (-> db (conj-initial-value!       [event-id group-id context-props])
                  (element/set-element-prop! [event-id group-id :input-count-increased? true])))))

(a/reg-event-db :x.app-elements/increase-input-count! increase-input-count!)

(defn decrease-input-count!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) group-id
  ; @param (integer) input-dex
  ;
  ; @example
  ;  (def db {:my-value ["First value" "Second value"]})
  ;  (r engine/decrease-input-count! db :my-group 1)
  ;  => {:my-value ["First value"]}
  ;
  ; @return (map)
  [db [event-id group-id input-dex]]
  (let [group-value-path    (r element/get-element-prop db group-id :value-path)
        group-value         (get-in db group-value-path)
        updated-group-value (vector/remove-nth-item group-value input-dex)]
       (assoc-in db group-value-path updated-group-value)))

(a/reg-event-db :x.app-elements/decrease-input-count! decrease-input-count!)

(defn stack-to-group-value!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) group-id
  ; @param (keyword) input-id
  ; @param (*) value
  ;
  ; @example
  ;  (def db {:my-value []})
  ;  (r engine/stack-to-group-value! db :my-group :my-input {:label "My value"})
  ;  => {:my-value [{:label "My value"}]}
  ;
  ; @example
  ;  (def db {:my-value ["First value" "Second value"]})
  ;  (r engine/stack-to-group-value! db :my-group :my-input "My value")
  ;  => {:my-value ["First value" "Second value" "My value"]}
  ;
  ; @return (map)
  [db [_ group-id _ value]]
  (let [group-value-path (r element/get-element-prop db group-id :value-path)]
       (update-in db group-value-path vector/conj-item-once value)))

(a/reg-event-db :x.app-elements/stack-to-group-value! stack-to-group-value!)

(defn unstack-from-group-value!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) group-id
  ; @param (keyword) input-id
  ; @param (integer) value-dex
  ;
  ; @example
  ;  (def db {:my-value ["First value" "Second value"]})
  ;  (r engine/unstack-from-group-value! db :my-group :my-input 0)
  ;  => {:my-value ["Second value"]}
  ;
  ; @return (map)
  [db [_ group-id _ value-dex]]
  (let [group-value-path (r element/get-element-prop db group-id :value-path)]
       (update-in db group-value-path vector/remove-nth-item value-dex)))

(a/reg-event-db :x.app-elements/unstack-from-group-value! unstack-from-group-value!)
