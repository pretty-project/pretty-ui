
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.item-browser.routes.events
    (:require [plugins.item-browser.core.events :as core.events]
              [plugins.item-browser.routes.subs :as routes.subs]
              [x.app-core.api                   :refer [r]]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn handle-route!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) browser-id
  ;
  ; @return (map)
  [db [_ browser-id]]
  ; XXX#5006
  ;
  ; Ha a get-derived-item-id függvény visszatérési értéke NIL, ...
  ; ... akkor a handle-route! függvény nem adja át a browse-item! függvénynek paraméterként
  ;     az aktuálisan böngészett elem azonosítóját.
  ; ... a body-did-mount függvény által alkalmazott use-root-item-id! függvény fogja eltárolni
  ;     a body komponens {:root-item-id "..."} paramétereként átadott azonosítót az aktuálisan
  ;     böngészett elem azonosítójaként.
  (if-let [derived-item-id (r routes.subs/get-derived-item-id db browser-id)]
          (r core.events/browse-item! db browser-id derived-item-id)
          (r core.events/browse-item! db browser-id)))
