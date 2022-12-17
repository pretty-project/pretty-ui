
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript web application framework
; https://monotech-hq.github.io/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns engines.item-browser.body.events
    (:require [engines.item-browser.core.events :as core.events]
              [engines.item-lister.body.events  :as body.events]
              [re-frame.api                     :refer [r]]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn body-did-mount
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) browser-id
  ; @param (map) body-props
  ; {:items-key (keyword)
  ;  :path-key (keyword)}
  ;
  ; @return (map)
  [db [_ browser-id {:keys [items-key path-key] :as browser-props}]]
  ; XXX#6177
  ; A body-did-mount függvény alkalmazza az item-lister body-did-mount függvényét, ...
  ;
  ; Az item-browser engine minden Pathom lekérés küldésekor elküldi a szerver számára a body komponens
  ; {:items-key ...} és {:path-key ...} tulajdonságát.
  (as-> db % (r body.events/body-did-mount        % browser-id browser-props)
             (r core.events/use-default-order-by! % browser-id)
             (r core.events/set-query-param!      % browser-id :items-key items-key)
             (r core.events/set-query-param!      % browser-id  :path-key  path-key)))

(defn body-will-unmount
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) browser-id
  ;
  ; @return (map)
  [db [_ browser-id]]
  ; XXX#6177
  (as-> db % (r core.events/reset-downloaded-item! % browser-id)
             (r body.events/body-will-unmount      % browser-id)))

(defn body-did-update
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) browser-id
  ; @param (map) body-props
  ;
  ; @return (map)
  [db [_ browser-id body-props]]
  ; XXX#6177
  ; XXX#1249 (source-code/cljs/engines/item_lister/body/effects.cljs)
  ;
  ; When the body component's :item-id parameter has been changed ...
  ; ... the clear-item-id! function has to be applied, otherwise the
  ;     update-item-id! function would ignores the updating if it sees
  ;     an id already set in the engine.
  ; ... the update-item-id! function derives the current id from the
  ;     body component's parameters.
  ; ... the use-item-id! function has to be applied according to:
  ;     XXX#0168 (source-code/cljs/engines/item_browser/core/events.cljs)
  (as-> db % (r body.events/body-did-update % browser-id body-props)
             (r core.events/clear-item-id!  % browser-id)
             (r core.events/update-item-id! % browser-id)
             (r core.events/use-item-id!    % browser-id)))
