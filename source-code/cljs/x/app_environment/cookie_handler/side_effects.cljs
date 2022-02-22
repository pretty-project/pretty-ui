
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2022.02.21
; Description:
; Version: 2.0.8
; Compatibility: x4.6.2



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-environment.cookie-handler.side-effects
    (:require [goog.net.cookies]
              [x.app-core.api :as a]
              [x.app-environment.cookie-handler.engine :as engine]))



;; -- Side-effect events ------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn store-browser-cookie!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) cookie-id
  ; @param (map) cookie-props
  ;  {:cookie-type (keyword)
  ;   :max-age (sec)
  ;   :secure (boolean)
  ;   :same-site (string)
  ;   :value (*)}
  [cookie-id {:keys [max-age secure same-site value] :as cookie-props}]
  (let [cookie-name (engine/cookie-id->cookie-name cookie-id cookie-props)
        cookie-body (str {:cookie-id cookie-id :value value})]
       (try (.set goog.net.cookies cookie-name cookie-body #js{:domain   engine/COOKIE-DOMAIN
                                                               :path     engine/COOKIE-PATH
                                                               :maxAge   max-age
                                                               :sameSite same-site
                                                               :secure   secure})
            (a/dispatch [:environment/cookie-set cookie-id cookie-props]))))

(defn remove-browser-cookie!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) cookie-id
  ; @param (map) cookie-props
  [cookie-id cookie-props]
  (let [cookie-name (engine/cookie-id->cookie-name cookie-id cookie-props)]
       (try (.remove goog.net.cookies cookie-name engine/COOKIE-PATH engine/COOKIE-DOMAIN)
            (a/dispatch [:environment/cookie-removed cookie-id]))))

(defn remove-browser-cookies!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [_]
  (try (.clear goog.net.cookies)
       (a/dispatch [:environment/cookies-removed])))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; WARNING! NON-PUBLIC! DO NOT USE!
(a/reg-fx :environment/store-browser-cookie! store-browser-cookie!)

; WARNING! NON-PUBLIC! DO NOT USE!
(a/reg-fx :environment/remove-browser-cookie! remove-browser-cookie!)

; WARNING! NON-PUBLIC! DO NOT USE!
(a/reg-fx :environment/remove-browser-cookies! remove-browser-cookies!)
