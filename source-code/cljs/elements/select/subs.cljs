
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript web application framework
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns elements.select.subs
    (:require [elements.input.subs :as input.subs]
              [re-frame.api        :as r :refer [r]]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn stored-value-not-passed?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) select-id
  ; @param (map) select-props
  ; {}
  ;
  ; @return (boolean)
  [db [_ select-id {:keys [value-path]}]]
  (let [stored-value (get-in db value-path)]
       (or (and (seqable? stored-value)
                (empty?   stored-value))
           (nil? stored-value))))

(defn required-warning?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) select-id
  ; @param (map) select-props
  ; {}
  ;
  ; @return (boolean)
  [db [_ select-id {:keys [required?] :as select-props}]]
  ; XXX#7551
  (and (= required? true)
       (r input.subs/input-visited? db select-id select-props)
       (r stored-value-not-passed?  db select-id select-props)))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; WARNING! NON-PUBLIC! DO NOT USE!
(r/reg-sub :elements.select/required-warning? required-warning?)
